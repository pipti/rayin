package ink.rayin.app.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.model.RestResponse;
import ink.rayin.app.web.model.UserIndexes;
import ink.rayin.app.web.model.UserIndexesUser;
import ink.rayin.app.web.model.UserOrganization;
import ink.rayin.app.web.annotation.OrgId;
import ink.rayin.app.web.annotation.UserId;
import ink.rayin.app.web.service.impl.UserIndexesService;
import ink.rayin.app.web.service.impl.UserOrganizationService;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.xmlpull.v1.XmlPullParserException;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * [PDF文件管理Controller].
 * [PDF文件管理 WebApp Rest API]
 * <h3>version info：</h3><br>
 * v1.0 2020-04-03 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
@Api(value = "PDFManagementController", description = "PDF管理API")
@RestController
public class PDFManagementController {
    private static Logger logger = LoggerFactory.getLogger(PDFManagementController.class);

    @Resource
    private UserOrganizationService userOrganizationService;

    @Resource
    UserIndexesService userIndexesService;

    @Value("${rayin.storage.url}")
    String storageUrl;
    /**
     * PDF文件查询
     *
     * @param orgId
     * @param userId
     * @return
     */
    @PostMapping(value = {"/organization/pdf/query"})
    public RestResponse organizationPDFQuery(@OrgId String orgId,
                                             @UserId String userId,
                                             @RequestBody JSONObject params,
                                             @RequestParam Integer pageCurrent,
                                             @RequestParam Integer pageSize)
            throws InvalidPortException, InvalidEndpointException, XmlPullParserException, InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, InternalException, InvalidBucketNameException, ErrorResponseException, XmlParserException {

        UserOrganization uo = new UserOrganization();
        uo.setUserId(userId);
        uo.setOrganizationId(orgId);
        uo = userOrganizationService.userOrganizationQueryOne(uo);
        if(uo == null){
            throw new RayinBusinessException("您无权访问该项目的文件内容！");
        }
//        if(StringUtils.isBlank(uo.getThirdStorageAccessKey()) ||
//                StringUtils.isBlank(uo.getThirdStorageSecretKey()) ||
//                StringUtils.isBlank(uo.getThirdStorageUrl()) ||
//                StringUtils.isBlank(uo.getThirdStorageBucket())){
//            throw new EPrintException("请在项目中设置第三方存储配置！");
//        }
        //封装自定义索引参数
//        Map<String,Object> map = new HashMap<>();
//        if (params != null) {
//            Set<String> keySet = params.getJSONObject("queryParams").keySet();
//            keySet.forEach(key -> {
//                map.put(key,params.get(key));
//            });
//        }
        //查询固定值索引
        UserIndexes userIndexes = new UserIndexes();
        userIndexes.setOrganizationId(orgId);
        userIndexes.setUserId(userId);
        Page<UserIndexes> page = new Page<>();
        page.setCurrent(1);
        page.setSize(100);
        IPage<UserIndexesUser> iPage = userIndexesService.userIndexQuery(page,userIndexes);
        List<UserIndexesUser> userIndexesUsers = iPage.getRecords();
        userIndexesUsers.forEach(userIndexesUser -> {
            params.put(userIndexesUser.getIndexName(),userIndexesUser.getValue());
        });
        Map<String,String> paramMap = createParamIndexMap(userIndexes,params);
        paramMap.remove("dimParam");
        //
//        try {
//            com.beisun.cms.client.common.util.Page<Map<String, Object>> pages = cmsClient.lookupObjectByBucket(orgId,pageCurrent,pageSize,paramMap);
//            return  RestResponse.success(pages);
//        } catch (CmsException e) {
//            e.printStackTrace();
//        }


//        MinioClient minioClient = new MinioClient(uo.getThirdStorageUrl(),
//                uo.getThirdStorageAccessKey(),
//                uo.getThirdStorageSecretKey());
//        List rs = new ArrayList();
//        if(StringUtils.isNotBlank(searchKey)){
//            Iterable<Result<Item>> results = minioClient.listObjects(uo.getThirdStorageBucket(),searchKey);
//            for (Result<Item> result : results) {
//                Item item = result.get();
//                rs.add(item);
//            }
//
//            return RestResponse.success(rs);
//        }else{
//            Iterable<Result<Item>> results = minioClient.listObjects(uo.getThirdStorageBucket());
//            for (Result<Item> result : results) {
//                Item item = result.get();
//                rs.add(item);
//            }
//            return RestResponse.success(rs);
//        }
        return RestResponse.success();
    }

    /**
     * PDF文件预览
     *
     * @param orgId
     * @param userId
     * @param jsonObject
     * @return
     */
    @ApiOperation(value="PDF预览", notes="PDF预览", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "项目ID", required = true,
                    dataType = "String", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true,
                    dataType = "String", paramType = "query",defaultValue = ""),
            @ApiImplicitParam(name = "jsonObject", value = "文件属性", required = true,
                    dataType = "JSONObject", paramType = "body",defaultValue = "")
    })
    @PostMapping(value = {"/organization/pdf/view"})
    public RestResponse organizationPDFView(@OrgId String orgId,
              @UserId String userId, @RequestBody(required = true) JSONObject jsonObject)
            throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, InvalidResponseException, InternalException, InvalidBucketNameException, ErrorResponseException,
            XmlParserException {
        UserOrganization uo = new UserOrganization();
        uo.setUserId(userId);
        uo.setOrganizationId(orgId);
        uo = userOrganizationService.userOrganizationQueryOne(uo);

        String objectId = jsonObject.getString("id");
        String bucket = jsonObject.getString("bucket");
        if(uo == null){
            throw new RayinBusinessException("您无权访问该项目的文件内容！");
        }
//        if(StringUtils.isBlank(uo.getThirdStorageAccessKey()) ||
//                StringUtils.isBlank(uo.getThirdStorageSecretKey()) ||
//                StringUtils.isBlank(uo.getThirdStorageUrl()) ||
//                StringUtils.isBlank(uo.getThirdStorageBucket())){
//            throw new RayinBusinessException("请在项目中设置第三方存储配置！");
//        }

//        MinioClient minioClient = new MinioClient(uo.getThirdStorageUrl(),
//                uo.getThirdStorageAccessKey(),
//                uo.getThirdStorageSecretKey());
        InputStream in = null;
//        try {
//            CmsObject cmsObject = cmsClient.simpleDownload(orgId,objectId);
//            in = cmsObject.getObjectContent();
//        } catch (CmsException e) {
//            e.printStackTrace();
//        }
//        InputStream in = minioClient.getObject(uo.getThirdStorageBucket(), objName);
        JSONObject r = new JSONObject();
        final Base64.Encoder encoder = Base64.getEncoder();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        byte[] dataBytes;
        while ((len = in.read(buffer)) != -1 ) {
            baos.write(buffer, 0, len);
        }
        dataBytes = baos.toByteArray();
        baos.flush();
        String res = encoder.encodeToString(dataBytes);
        r.put("pdfFile",res);
        in.close();
        return RestResponse.success(r);
    }


    @GetMapping(value = {"/organization/pdf/url/{id}"})
    public RestResponse organizationPDFurl(@OrgId String orgId,
                                            @UserId String userId,@PathVariable("id") String objectId) {
        UserOrganization uo = new UserOrganization();
        uo.setUserId(userId);
        uo.setOrganizationId(orgId);
        uo = userOrganizationService.userOrganizationQueryOne(uo);

        if(uo == null){
            throw new RayinBusinessException("您无权访问该项目的文件内容！");
        }
//        if(StringUtils.isBlank(uo.getThirdStorageAccessKey()) ||
//                StringUtils.isBlank(uo.getThirdStorageSecretKey()) ||
//                StringUtils.isBlank(uo.getThirdStorageUrl()) ||
//                StringUtils.isBlank(uo.getThirdStorageBucket())){
//            throw new RayinBusinessException("请在项目中设置第三方存储配置！");
//        }

        String url = null;
//        try {
//            url = cmsClient.privateDownloadUrl(orgId,objectId,String.valueOf(System.currentTimeMillis()+ 60 * 60 *1000));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        url = storageUrl + url;
        return RestResponse.success(url);
    }
    /**
     * 创建索引信息
     * @return
     */
    private Map createParamIndexMap(UserIndexes userIndexes,Map<String,Object> outerIndexMap) {
        Map<String,Object> userMeta = new HashMap<>();
//        userMeta.put("sys_id", "test");
//        userMeta.put("bill_class", "billclass");
//        userMeta.put("sub_class", "sub_class");
//        userMeta.put("index_no", "index_no");
//        //文件后缀
//        userMeta.put("file_type","pdf");
//        userMeta.put("organization_id",userIndexes.getOrganizationId());
        //增加自定义索引
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
}