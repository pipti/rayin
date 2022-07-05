package ink.rayin.app.web.controller;

import com.alibaba.fastjson.JSONObject;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.model.Rayin;
import ink.rayin.app.web.model.RestResponse;
import ink.rayin.app.web.service.IRestPdfCreateService;
import ink.rayin.htmladapter.base.utils.RayinException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * [PDF生成接口服务Controller].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-09 Eric Wang create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
@RestController
public class PDFCreatorController {
    private static Logger logger = LoggerFactory.getLogger(PDFCreatorController.class);
    @Resource
    IRestPdfCreateService iRestPdfCreateService;
    @Resource
    AsyncConfigurer asyncConfigurer;

    @Resource
    RedisTemplateUtil redisTemplateUtil;
//    /**
//     * 用户模板生成接口
//     *
//     * @param parameter
//     * @return
//     */
//    @CrossOrigin
//    @PostMapping(value = "/template/pdf/create", produces = MediaType.APPLICATION_JSON_VALUE)
//    public RestResponse userTemplatePdfCreator(@RequestHeader("authorization") String header,
//                                     @RequestBody JSONObject parameter) throws Exception {
//        String accessKey = null;
//        if(StringUtils.isNotBlank(header)){
//            String[] s = header.split(":");
//            accessKey = s[0];
//        }
//
//        JSONObject eprintJ = parameter.getJSONObject("eprint");
//
//        if(eprintJ == null){
//            throw new EPrintException("参数错误，请确认是否有eprint节点");
//        }
//        EPrint eprint = eprintJ.toJavaObject(EPrint.class);
//        String validateStr = beanValidate(eprint);
//        if(StringUtils.isNotBlank(validateStr)){
//            // return RestResponse.failed(BusinessCodeMessage.HTTP_BAD_REQUEST.getCode(),validateStr);
//            throw new EPrintException(validateStr);
//        }
//
//        /** "eprint":{
//            "templateId":"",
//            "templateVersion":"",
//            "timeStamp":1579404181322,
//            "transactionNo":"交易流水号",
//            "traceId":"跟踪流水号",
//            "callbackUrl":""
//        }*/
//
//        return RestResponse.success(restPdfCreateService.createPdfByTemplateId(accessKey,eprint,parameter));
//    }


    /**
     * 项目模板生成接口
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/org/template/pdf/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse orgTemplatePdfCreator(@RequestHeader("accessKey") String accessKey,
                                              @RequestBody JSONObject parameter) throws Exception {
        if(StringUtils.isBlank(accessKey)){
            return RestResponse.failed(BusinessCodeMessage.ACC_KEY_ERROR);
        }

        JSONObject rayinJson = parameter;
        String data = parameter.getString("body");
        if(rayinJson == null){
            //throw new RayinException("参数错误，请确认是否有rayin节点");
            return RestResponse.failed(BusinessCodeMessage.HTTP_BAD_REQUEST.getCode(),"参数错误，请确认是否有rayin节点");
        }
        Rayin rayin = rayinJson.toJavaObject(Rayin.class);
        String validateStr = beanValidate(rayin);
        if(StringUtils.isNotBlank(validateStr)){
             return RestResponse.failed(BusinessCodeMessage.HTTP_BAD_REQUEST.getCode(),validateStr);
            //throw new RayinException(validateStr);
        }

        /** "rayin":{
         "templateId":"",
         "templateVersion":"",
         "timeStamp":1579404181322,
         "transactionNo":"交易流水号",
         "traceId":"跟踪流水号",
         "callbackUrl":"",
         "sysId":""
         }*/
        String transactionNo = rayin.getTransactionNo();
        Map<String,String> map = iRestPdfCreateService.createPdfByTemplateId(accessKey, rayin, data,null);

        map.put("transactionNo",transactionNo);
        return RestResponse.success(map);
    }

    /**
     * 项目模板生成接口
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/org/template/pdf/air/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse orgTemplatePdfaAirCreator(@RequestHeader("accessKey") String accessKey,
                                              @RequestBody JSONObject parameter) throws Exception {
        if(StringUtils.isBlank(accessKey)){
            return RestResponse.failed(BusinessCodeMessage.ACC_KEY_ERROR);
        }

        JSONObject eprintJ = parameter;
        String data = parameter.getString("body");
        if(eprintJ == null){
            throw new RayinException("参数错误，请确认是否有rayin节点");
        }
        Rayin eprint = eprintJ.toJavaObject(Rayin.class);
        String validateStr = beanValidate(eprint);
        if(StringUtils.isNotBlank(validateStr)){
             return RestResponse.failed(BusinessCodeMessage.HTTP_BAD_REQUEST.getCode(),validateStr);
           // throw new RayinException(validateStr);
        }

        /** "rayin":{
         "templateId":"",
         "templateAlias":"",
         "organizationId":"",
         "templateVersion":"",
         "timeStamp":1579404181322,
         "transactionNo":"交易流水号",
         "callbackUrl":"",
         "sysId":""
         }*/
        String transactionNo = eprint.getTransactionNo();
        Map<String,String> map = iRestPdfCreateService.createPdfByTemplateId(accessKey,eprint,data,null);
        map.put("transactionNo",transactionNo);
        return RestResponse.success(map);
    }

    /**
     * 项目模板生成接口
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/org/template/pdf/createAsync", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse orgTemplatePdfCreatorAsync(@RequestHeader("accessKey") String accessKey,
                                              @RequestBody JSONObject parameter) throws Exception {
        if(StringUtils.isBlank(accessKey)){
            return RestResponse.failed(BusinessCodeMessage.ACC_KEY_ERROR);
        }

        JSONObject eprintJ = parameter;
        String data = parameter.getString("body");
        if(eprintJ == null){
            throw new RayinException("参数错误，请确认是否有rayin节点");
        }
        Rayin eprint = eprintJ.toJavaObject(Rayin.class);
        String validateStr = beanValidate(eprint);
        if(StringUtils.isNotBlank(validateStr)){
             return RestResponse.failed(BusinessCodeMessage.HTTP_BAD_REQUEST.getCode(),validateStr);
          //  throw new RayinException(validateStr);
        }

//        ThreadPoolTaskExecutorHelper threadPoolTaskExecutorHelper = (ThreadPoolTaskExecutorHelper) asyncConfigurer.getAsyncExecutor();
//        System.err.println(threadPoolTaskExecutorHelper);
//        Executor executor = asyncConfigurer.getAsyncExecutor();
//        Object[] objects = new Object[]{accessKey,eprint,data};
//        Thread thread = new Thread(() -> {
//            try {
//                Class c = restPdfCreateService.getClass();
//                System.err.println(c.getName());
//                c.getMethod("createPdfByTemplateIdAsync",String.class,EPrint.class,String.class).invoke(restPdfCreateService,objects);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        });
//        executor.execute(thread);
        iRestPdfCreateService.createPdfByTemplateIdAsync(accessKey,eprint,data,null);
        String transactionNo = eprint.getTransactionNo();
        Map<String,String> map = new HashMap<>();
        map.put("transactionNo",transactionNo);
        return RestResponse.success(map);
    }
//    /**
//     * 用户动态模板生成
//     *
//     * @param parameter
//     * @return
//     */
//    @PostMapping(value = "/dync/template/pdf/create", produces = MediaType.APPLICATION_JSON_VALUE)
//    public RestResponse userDyncTemplatePdfCreator(@RequestHeader("authorization") String header,
//                                               @RequestBody JSONObject parameter) throws Exception {
//        String accessKey = null;
//        if(StringUtils.isNotBlank(header)){
//            String[] s = header.split(":");
//            accessKey = s[0];
//        }
//
//        JSONObject eprintJ = parameter.getJSONObject("eprint");
//
//        if(eprintJ == null){
//            throw new RayinException("参数错误，请确认是否有eprint节点");
//        }
//        EPrint eprint = eprintJ.toJavaObject(EPrint.class);
//        String validateStr = beanValidate(eprint);
//        if(StringUtils.isNotBlank(validateStr)){
//            // return RestResponse.failed(BusinessCodeMessage.HTTP_BAD_REQUEST.getCode(),validateStr);
//            throw new RayinException(validateStr);
//        }
//
//        /** "eprint":{
//         "templateId":"",
//         "templateVersion":"",
//         "timeStamp":1579404181322,
//         "transactionNo":"交易流水号",
//         "traceId":"跟踪流水号",
//         "fileName":"文件名称",
//         "callbackUrl":""
//         }*/
//
//        return RestResponse.success(restPdfCreateService.createPdfByTemplateId(accessKey,eprint,parameter));
//    }

    /**
     * 文件核验
     * @param file
     * @return
     */
    @PostMapping("/pdf/check")
    @ResponseBody
    public RestResponse upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
        JSONObject result;
        try {
            InputStream inputStream = file.getInputStream();
            result = iRestPdfCreateService.fileCheck(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
           return RestResponse.failed(BusinessCodeMessage.FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
        int resCode = result.getInteger("code");
        return RestResponse.success(result);
    }

    /**
     * 验证某个bean的参数
     * @param object
     */
    public <T> String beanValidate(T object) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<T>> set = validator.validate(object);
        StringBuffer s = new StringBuffer(200);
        for (ConstraintViolation<T> constraintViolation : set) {
            s.append(constraintViolation.getMessage());
            // System.out.println(constraintViolation.getMessage());
        }
        return s.toString();
    }

//    @GetMapping("/test")
//    public void test() {
//        Long v = redisTemplateUtil.get("valueKey1-0",Long.class);
//        if (v == null) {
//            redisTemplateUtil.set("valueKey1-0",0L);
//            redisTemplateUtil.set("valueKey1",0L);
//        }
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            Thread t = new Thread(() -> {
//                for (int j = 0; j < 100; j ++) {
//                    optimisticRedisLockSupport.add("valueKey1", 1L);
//                }
//            },"n"+i);
//            t.start();
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        long end = System.currentTimeMillis();
//        System.err.println("time:" + (end-start));
//        System.err.println("value:" + redisTemplateUtil.get("valueKey1",Long.class));
//    }
//
//    @GetMapping("/test2")
//    public void test2() {
//        Long v = redisTemplateUtil.get("valueKey2",Long.class);
//        if (v == null) {
//            redisTemplateUtil.set("valueKey2",0L);
//        }
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 100; i++) {
//            Thread t = new Thread(() -> {
//                for (int j = 0; j < 100; j ++) {
//                    redisLock.add("valueKey2", 1L);
//                }
//            },"n"+i);
//            t.start();
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        long end = System.currentTimeMillis();
//        System.err.println("time:" + (end-start));
//        System.err.println("value:" + redisTemplateUtil.get("valueKey2",Long.class));
//    }

}