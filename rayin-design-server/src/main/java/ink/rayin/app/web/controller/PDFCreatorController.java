package ink.rayin.app.web.controller;

import com.alibaba.fastjson.JSONObject;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.model.Rayin;
import ink.rayin.app.web.model.RestResponse;
import ink.rayin.app.web.model.UserModel;
import ink.rayin.app.web.service.IRestPdfCreateService;
import ink.rayin.app.web.service.ITokenService;
import ink.rayin.htmladapter.base.utils.RayinException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
    private ITokenService iTokenService;
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

    @PostMapping(value = "/rayin/token/getToken")
    public RestResponse<String> getToken(@RequestBody Map<String,String> map) {
        String accKey = map.get("accessKey");
        String secretKey = map.get("secretKey");
        String token = iTokenService.getToken(accKey,secretKey);
        if (token == null) {
            return RestResponse.failed(-1,"授权失败");
        }
        return RestResponse.success(token);
    }

    /**
     * 项目模板生成接口
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/rayin/org/template/pdf/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse orgTemplatePdfCreator(@RequestHeader("rayin-token") String token,
                                              @RequestBody JSONObject parameter) throws Exception {

       UserModel userModel = iTokenService.decodeToken(token);

        String accessKey = userModel.getUsername();

        if(StringUtils.isBlank(accessKey)){
            return RestResponse.failed(BusinessCodeMessage.TOKEN_FAILED);
        }

        JSONObject rayinJson = parameter;
        //String data = parameter.getString("body");
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
       //String transactionNo = rayin.getTransactionNo();
        Map<String,String> map = iRestPdfCreateService.createPdfByTemplateId(accessKey, rayin);

        map.put("transactionNo", rayin.getTransactionNo());
        return RestResponse.success(map);
    }

    /**
     * 项目模板生成接口
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/rayin/org/template/pdf/air/create", produces = MediaType.APPLICATION_JSON_VALUE)
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
        Rayin rayin = eprintJ.toJavaObject(Rayin.class);
        String validateStr = beanValidate(rayin);
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
        String transactionNo = rayin.getTransactionNo();
        Map<String,String> map = iRestPdfCreateService.createPdfByTemplateId(accessKey, rayin);
        map.put("transactionNo",transactionNo);
        return RestResponse.success(map);
    }

    /**
     * 项目模板生成接口
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/rayin/org/template/pdf/createAsync", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponse orgTemplatePdfCreatorAsync(@RequestHeader("accessKey") String accessKey,
                                              @RequestBody JSONObject parameter) throws Exception {
        if(StringUtils.isBlank(accessKey)){
            return RestResponse.failed(BusinessCodeMessage.ACC_KEY_ERROR);
        }

        JSONObject rayinJson = parameter;
        //String data = parameter.getString("data");
        if(rayinJson == null){
            throw new RayinException("参数错误，请确认是否有rayin节点");
        }
        Rayin rayin = rayinJson.toJavaObject(Rayin.class);
        String validateStr = beanValidate(rayin);
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
        iRestPdfCreateService.createPdfByTemplateIdAsync(accessKey, rayin);
        String transactionNo = rayin.getTransactionNo();
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
     * 验证某个bean的参数
     * @param object
     */
    public <T> String beanValidate(T object) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<T>> set = validator.validate(object);
        StringBuffer s = new StringBuffer(200);
        for (ConstraintViolation<T> constraintViolation : set) {
            s.append(constraintViolation.getMessage() + ",");
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