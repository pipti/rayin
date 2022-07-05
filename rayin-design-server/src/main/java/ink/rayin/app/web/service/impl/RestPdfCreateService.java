
package ink.rayin.app.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.dao.OrganizationIndexesMapper;
import ink.rayin.app.web.dao.OrganizationMapper;
import ink.rayin.app.web.dao.UserKeysMapper;
import ink.rayin.app.web.dao.UserTemplateMapper;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.model.*;
import ink.rayin.app.web.service.IMemoryCapacityService;
import ink.rayin.app.web.service.IRestPdfCreateService;
import ink.rayin.app.web.utils.DecryptUtil;
import ink.rayin.app.web.utils.DesUtil;
import ink.rayin.app.web.utils.ShortUrlUtil;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.model.tplconfig.RayinMeta;

import ink.rayin.tools.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * [接口调用的pdf生成 服务类].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-09 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
@Service
@Slf4j
public class RestPdfCreateService implements IRestPdfCreateService {

    @Resource
    UserKeysMapper userKeysMapper;
    @Resource
    OrganizationMapper organizationMapper;
    @Resource
    UserTemplateMapper userTemplateMapper;
    @Resource
    PdfGenerator pdfCreateService;
    @Resource
    IMemoryCapacityService iMemoryCapacityService;

    @Resource
    OrganizationIndexesMapper organizationIndexesMapper;

    @Resource
    RedisTemplateUtil redisTemplateUtil;
    @Value("${mychain.rest.bizid}")
    String bizid;

    @Value("${eprint.storage.url}")
    String cmsUrl;

    @Override
    public void init() throws Exception {
        pdfCreateService.init();
    }
    private static int i = 1;
    @Override
    public Map<String,String> createPdfByTemplateId(String accessKey, Rayin rayin, String body, BaseParam baseParam) throws Exception {
        Organization org = organizationMapper.selectOne(new QueryWrapper<Organization>().eq("access_key",accessKey));
                //.eq("organization_id",ep.getOrganizationId()));
        if(org == null){
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.ACC_KEY_ERROR);
        }
        if (!iMemoryCapacityService.check(org.getOrganizationId())) {
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.OUT_OF_MEMORY);
        }
        Date today = new Date();
//        if(uk.getEndTime() != null && uk.getEndTime().compareTo(today) < 0 ){
//            throw new EPrintException("accessKey已过期！");
//        }
        String desStr = DesUtil.decrypt(body,org.getSecretKey());
        JSONObject data = JSONObject.parseObject(desStr);
        UserTemplate userTemplate = null;

        if(StringUtils.isBlank(rayin.getTemplateAlias()) && StringUtils.isBlank(rayin.getTemplateId())){
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.TEMPLATE_OR_ALIAS_NOT_EMPTY_AT_THE_SAME_TIME);
        }


        if(StringUtils.isNotBlank(rayin.getTemplateAlias())){
            userTemplate = userTemplateMapper.selectOne(
                    new QueryWrapper<UserTemplate>().eq("organization_id",org.getOrganizationId())
                            //          .and(i->i.eq("template_id",rayin.getTemplateId()).or().eq("alias",rayin.getTemplateAlias())));
                            .eq("alias",rayin.getTemplateAlias()).eq("template_version",rayin.getTemplateVersion()));
        }else if(StringUtils.isNotBlank(rayin.getTemplateId())){
                userTemplate = userTemplateMapper.selectOne(
                        new QueryWrapper<UserTemplate>().eq("organization_id",org.getOrganizationId())
                         //       .and(i->i.eq("template_id",rayin.getTemplateId()).or().eq("alias",rayin.getTemplateAlias())));
                        .eq("template_id",rayin.getTemplateId()).eq("template_version",rayin.getTemplateVersion()));
        }

        if(userTemplate == null){
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.TEMPLATE_NOT_FOUND);
        }

        if(userTemplate.getTemplateReleaseStatus() != null && userTemplate.getTemplateReleaseStatus() != true){
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.TEMPLATE_NOT_RELEASE);
        }

        if(userTemplate.getStartTime() != null && today.compareTo(userTemplate.getStartTime()) < 0){
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.TEMPLATE_NOT_START);
        }

        if(userTemplate.getEndTime() != null && today.compareTo(userTemplate.getEndTime()) > 0){
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.TEMPLATE_TIME_OUT);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String secretKey = DecryptUtil.encrypt( org.getOrganizationId() + "||" + rayin.getTransactionNo());
        pdfCreateService.generateEncryptPdfStreamByConfigStr(userTemplate.getTplConfig(), data, baos,secretKey);
        Long size = Long.valueOf(baos.toByteArray().length);

        if (!iMemoryCapacityService.checkAndAdd(org.getOrganizationId(),size)) {
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.OUT_OF_MEMORY);
        }
//        String filename = "D:/2020118242SG1015005327_15B113E15E42416AB4FB00036225EEC5_unlock.pdf";
//        i++;
//        ByteArrayInputStream in = new ByteArrayInputStream(baos.toByteArray());
//        FileOutputStream fileOutputStream = new FileOutputStream(new File(filename));
//        BufferedOutputStream bf = new BufferedOutputStream(fileOutputStream);
//        byte[] buff = new byte[1024];
//        int n;
//        while ((n = in.read(buff))!= -1) {
//            bf.write(buff,0,n);
//        }
//        if (bf != null) {
//            try {
//                bf.close();
//            } catch (IOException e) {
//            }
//        }
//        if (fileOutputStream != null) {
//            try {
//                fileOutputStream.close();
//            } catch (IOException e) {
//            }
//        }
//        return "";
//        FileInputStream fileInputStream = new FileInputStream(filename);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buff = new byte[1024];
//        int n;
//        while ((n = fileInputStream.read(buff))!= -1) {
//            baos.write(buff,0,n);
//        }
//        if (baos != null) {
//            try {
//                baos.close();
//            } catch (IOException e) {
//            }
//        }
//        if (fileInputStream != null) {
//            try {
//                fileInputStream.close();
//            } catch (IOException e) {
//            }
//        }
        ByteArrayInputStream inStreamForMd5 = new ByteArrayInputStream(baos.toByteArray());
        String md5 = FileUtil.getFileMd5(inStreamForMd5);
        //获取自定义索引
        Map<String,Object> outerIndexMap = new HashMap<>();
//        List<OrganizationIndexesUser> organizationIndexesUsers = userOrganizationApiClient.userOrganizationIndexGetAll(org.getOrganizationId(),1,100);
        Page<OrganizationIndexesUser> page = new Page<>();
        page.setCurrent(1);
        page.setSize(100);
        List<OrganizationIndexesUser> organizationIndexesUsers = organizationIndexesMapper.organizationIndexesQuery(page,org.getOrganizationId()).getRecords();
        organizationIndexesUsers.forEach(organizationIndexesUser -> {
            outerIndexMap.put(organizationIndexesUser.getIndexName(), JSONPath.eval(data,organizationIndexesUser.getJsonPath()));
        });

        Map indexMap = createIndexMap(outerIndexMap);
        ByteArrayInputStream inStream = new ByteArrayInputStream(baos.toByteArray());
        //报文中的标记
//        String isDeposit = (String) JSONPath.eval(data,"$.eprint.isDeposit");
//        String hash = "";
//        if (org.isDeposit() && (StringUtils.isNotBlank(isDeposit) && "true".equals(isDeposit.trim()))) {
//            hash = iDepositService.depositData(md5,baseParam);
//        }
        //是否质检
//        if (true) {
//            pdfQualityService.pdfQuality(baos);
//        }
        //TODO 索引中添加区块链信息

        String transactionNo = rayin.getTransactionNo();
        indexMap.put("sys_id", rayin.getSysId());//系统编号
        indexMap.put("transaction_no", transactionNo); //流水号
        indexMap.put("organization_id",org.getOrganizationId());//项目ID
        indexMap.put("template_id",StringUtils.isBlank(rayin.getTemplateId())?"":rayin.getTemplateId());//模板编号
        indexMap.put("template_alias",StringUtils.isBlank(rayin.getTemplateAlias())?"":rayin.getTemplateAlias());//模板编号
        indexMap.put("template_version",rayin.getTemplateVersion());//模板版本
        indexMap.put("file_type","pdf");//文件类型
        //TODO 异步流程支持质检
        //indexMap.put("quality","true");//是否质检完成
        //indexMap.put("quality_info","");//检质检详情
        //TODO 异常捕获
        // TODO 修改为生成至对象存储
//        try{
//            String objectId = cmsService.upload(md5,org.getOrganizationId(),null,inStream,true,"","",indexMap);
//            String privateUrl = cmsUrl+cmsService.privateUrl(org.getOrganizationId(),objectId);
//            String shortUrl = createShortUrl(objectId,org.getOrganizationId());
//            log.info("短链接：" + shortUrl);
//            if (baseParam != null) {
//                baseParam.appendParam("deposit",md5)
//                        .appendParam("deposit",org.getOrganizationId())
//                        .appendParam("deposit",objectId)
//                        .appendParam("pdfQuality",baos);
//            }
//            Map<String, String> retMap = new HashMap<>();
//            retMap.put("privateUrl",privateUrl);
//            retMap.put("shortUrl",shortUrl);
//            return retMap;
//        }catch(CmsException e){
//            throw new RayinBusinessException(e.getMessage());
//        }
        return null;
    }
//
//    @TaskDispense(name = "createPdf",taskId = "1")
//    @Async
//    @Override
//    public void createPdfByTemplateIdAsync(String accessKey, Rayin ep, String data, BaseParam baseParam) throws Exception {
//        createPdfByTemplateId(accessKey,ep,data, baseParam);
//    }


    /**
     * 创建索引信息
     * @return
     */
    private Map createIndexMap(Map<String,Object> outerIndexMap) {
        Map<String,Object> userMeta = new HashMap<>();
        if (outerIndexMap != null) {
            Set<String> keySet = outerIndexMap.keySet();
            keySet.forEach(key -> {
                Object obj = outerIndexMap.get(key);
                if (obj == null) {
                    userMeta.put(key,"");
                } else {
                    userMeta.put(key,obj);
                }

            });
        }
        return userMeta;
    }



    private String createShortUrl(String objectId,String organizationId) {
        String shortUrl = ShortUrlUtil.shortUrl(objectId+organizationId,10);
        String key = KeyUtil.makeKey("Rayin","-",shortUrl);
        String res = redisTemplateUtil.get(key,String.class);
        if (StringUtils.isBlank(res)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("organizationId",organizationId);
            jsonObject.put("objectId",objectId);
            //15天过期
            redisTemplateUtil.save(key,jsonObject.toJSONString(),15 * 24 *3600L);
            return shortUrl;
        }
        return createShortUrl(objectId,organizationId);
    }
}
