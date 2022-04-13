package ink.rayin.app.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.annotation.OrgId;
import ink.rayin.app.web.annotation.UserId;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.model.RestResponse;
import ink.rayin.app.web.model.UserTemplate;
import ink.rayin.app.web.model.UserTemplateApprove;
import ink.rayin.app.web.service.impl.UserTemplateService;
import ink.rayin.htmladapter.base.utils.RayinException;
import ink.rayin.tools.utils.Base64Util;
import ink.rayin.tools.utils.DigestUtil;
import ink.rayin.tools.utils.GZipUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * [模板管理Controller].
 * [用户模板管理 WebApp Rest API,包括模板的增删改查]
 * <h3>version info：</h3><br>
 * v1.0 2020-03-01 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
@CrossOrigin
@RestController
public class TemplateManagementController {
    private static Logger logger = LoggerFactory.getLogger(TemplateManagementController.class);
    @Resource
    UserTemplateService userTemplateService;

    /**
     * 模板查询
     * @param  pageCurrent
     * @param pageSize
     * @param key
     * @return
     */

    @GetMapping(value = {"/template/query/{key}","/template/query"})
    public RestResponse templateQuery(@UserId String userId,
                                      @OrgId String orgId,
                                      @RequestParam Integer pageCurrent, @RequestParam Integer pageSize,
                                      @PathVariable(required = false) String key,
                                      @RequestParam(required = false) Boolean delFlag) throws Exception {

        UserTemplate ut = new UserTemplate();
        ut.setName(key);
        //ut.setUserId(userId);
        ut.setOrganizationId(orgId);
        ut.setDelFlag(delFlag == null?false:delFlag);

        Page page = new Page(pageCurrent,pageSize);
        IPage<UserTemplate> pages =  userTemplateService.userTemplateQuery(page,ut);
        return RestResponse.success(pages);
    }


    /**
     * 模板插入、更新
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/template/save")
    public RestResponse templateSave(@UserId String userId, @OrgId String orgId,@RequestBody UserTemplate parameter) throws Exception {
        if(StringUtils.isBlank(parameter.getName())){
            throw new RayinException("请设置模板名称！");
        }

        // parameter.setUserId(userId);
        parameter.setOrganizationId(orgId);
        // 更新操作，更新原有记录
        if(StringUtils.isNotBlank(parameter.getTemplateId()) && StringUtils.isBlank(parameter.getNewFlag())){
            parameter.setUpdateTime(new Date());
            if(userTemplateService.userTemplateSave(parameter) > 0){
                return RestResponse.success();
            }else{
                return RestResponse.failed(BusinessCodeMessage.FAILED);
            }
        }
        // 新增操作
        parameter.setUserId(userId);
        parameter.setCreateTime(new Date());
        if(userTemplateService.userTemplateSave(parameter) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }


    @PostMapping(value = "/template/logicalDel")
    public RestResponse templateLogicalDel(@UserId String userId,
                                           @OrgId String orgId,
                                           @RequestBody UserTemplate parameter){
//        if(!userId.equals(parameter.getUserId())){
//            throw new EPrintException("操作的资源与用户不一致，请重新登录！");
//        }
        if(userTemplateService.userTemplateLogicalDel(parameter) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    @PostMapping(value = "/template/del")
    public RestResponse templateDel(@UserId String userId,@OrgId String orgId,@RequestBody UserTemplate parameter){
        if(!userId.equals(parameter.getUserId()) || !orgId.equals(parameter.getOrganizationId())){
            throw new RayinException("只能清理自己创建的模板！");
        }

        if(userTemplateService.userTemplateDel(parameter) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 线上模板导出
     * @param userId
     * @param orgId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/template/online/export")
    public RestResponse templateOnlineExport(@UserId String userId,@OrgId String orgId,@RequestBody UserTemplate parameter){
        String tplData = JSON.toJSONString(userTemplateService.userTemplateOnlineExport(parameter));
        String commpressStr = GZipUtil.compress(tplData);
        String base64Str = Base64Util.encode("online"+commpressStr);
        String md5Str = DigestUtil.md5Hex(base64Str);
        String data = md5Str + base64Str;
        return RestResponse.success(data);
    }

    /**
     * 线下模板导出
     * @param userId
     * @param orgId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/template/offline/export")
    public RestResponse templateOfflineExport(@UserId String userId,@OrgId String orgId,@RequestBody UserTemplate parameter){
        String tplData = userTemplateService.userTemplateOfflineExport(parameter);
        String commpressStr = GZipUtil.compress(tplData);
        String base64Str = Base64Util.encode("offline"+commpressStr);
        String md5Str = DigestUtil.md5Hex(base64Str);
        String data = md5Str + base64Str;
        return RestResponse.success(data);
    }



    /**
     * 线上模板导入
     * @param userId
     * @param orgId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/template/import")
    public RestResponse templateImport(@UserId String userId,@OrgId String orgId,@RequestBody HashMap parameter){
        String importData = (String)parameter.get("importData");
        boolean newTemplateFlag = (boolean)parameter.get("overwriteFlag");
//        boolean newElementFlag = (boolean)parameter.get("newElement");

        String md5Str = importData.substring(0,32);
        String base64Str = importData.substring(32);
        if(!md5Str.equalsIgnoreCase(DigestUtil.md5Hex(base64Str))){
            throw new RayinException("导入的模板文件错误，该文件可能不是导出的模板文件");
        }
        String commpressStr = Base64Util.decode(base64Str);
        if(!commpressStr.substring(0,6).equals("online")){
            throw new RayinException("导入的模板非线上模板，该文件不能用于线上导入！");
        }else{
            commpressStr = commpressStr.substring(6);
        }
        String unCommpressStr = GZipUtil.uncompress(commpressStr);
        UserTemplate data = JSONObject.parseObject(unCommpressStr,UserTemplate.class);
        data.setUserId(userId);
        data.setOrganizationId(orgId);
        if(userTemplateService.userTemplateImport(userId,orgId,data,newTemplateFlag) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }


    @PostMapping(value = "/template/view")
    public RestResponse templateView(@UserId String userId,
                                     @RequestBody UserTemplate parameter) throws Exception {
        return RestResponse.success(userTemplateService.userTemplateView(parameter));
    }

//    @PostMapping(value = "/template/test")
//    public RestResponse templateTest(@UserId String userId,
//                                     @RequestBody UserTemplate parameter) throws Exception {
//
//        return RestResponse.success(userTemplateService.userTemplateTest(parameter));
//    }

    @PostMapping(value = "/template/tplGenerate")
    public RestResponse templateGenerate(@UserId String userId,
                                     @RequestBody UserTemplate parameter) throws Exception {
        return RestResponse.success(userTemplateService.templateGenerate(parameter));
    }


    @PostMapping(value = "/template/testData/save")
    public RestResponse templateTestDataSave(@UserId String userId,@RequestBody UserTemplate parameter){
        if(!userId.equals(parameter.getUserId())){
            throw new RayinException("操作的资源与用户不一致，请重新登录！");
        }
        if(userTemplateService.userTemplateTestDataSave(parameter) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }



    /**
     * 模板恢复
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/template/resume")
    public RestResponse templateResume(@UserId String userId, @RequestBody UserTemplate parameter) {
        //TODO 锁定状态不能操作非本人的模板
//        if(!userId.equals(parameter.getUserId())){
//            throw new EPrintException("操作的资源与用户不一致，请重新登录！");
//        }
        if (userTemplateService.userTemplateResume(parameter) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }

    }

    /**
     * 模板分享至其他项目
     * @param userId
     * @param orgId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/template/publishToOtherOrgPendingApproval")
    public RestResponse templatePublishToOtherOrgPendingApproval(@UserId String userId,
                                                                 @OrgId String orgId,
                                                                 @RequestBody UserTemplateApprove parameter) throws IllegalAccessException, IOException, InstantiationException {

        if(userTemplateService.userTemplatePublishToOtherOrgPendingApproval(userId,orgId,parameter) > 0){
            return RestResponse.success(BusinessCodeMessage.SUCCESS.getCode(),"已发送同步请求，待项目管理成员审核通过后生效！");
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 模板同步待审核查询
     * @param  pageCurrent
     * @param pageSize
     * @param key
     * @return
     */
    @GetMapping(value = {"/template/approve/waitQuery/{key}","/template/approve/waitQuery"})
    public RestResponse templateApproveWaitQuery(@UserId String userId,
                                      @OrgId String orgId,
                                      @RequestParam Integer pageCurrent,@RequestParam Integer pageSize,@RequestParam boolean waitFlag,
                                      @PathVariable(required = false) String key) throws Exception {

        UserTemplateApprove uta = new UserTemplateApprove();
        uta.setName(key);
        //ut.setUserId(userId);
        uta.setToOrganizationId(orgId);

        Page page = new Page(pageCurrent,pageSize);
        IPage<UserTemplateApprove> pages;
        if(waitFlag == true){
            pages =  userTemplateService.userTemplateApproveWaitQuery(page,uta);
        }else{
            pages =  userTemplateService.userTemplateApproveFinishQuery(page,uta);
        }

        return RestResponse.success(pages);
    }

    /**
     * 模板审核PDF查看
     * @param  userId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/template/approve/pdfView")
    public RestResponse templateApprovePdfView(@UserId String userId,
                                     @RequestBody UserTemplateApprove parameter) throws Exception {
        return RestResponse.success(userTemplateService.userTemplateApprovePdfView(parameter));
    }

    /**
     * 模板分享至其他项目
     * @param userId
     * @param orgId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/template/approve/check")
    public RestResponse templateApproveCheck(@UserId String userId,
                                                                 @OrgId String orgId,
                                                                 @RequestBody UserTemplateApprove parameter) throws IllegalAccessException, IOException, InstantiationException {

        if(userTemplateService.userTemplateApproveCheck(userId,orgId,parameter) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }
}
