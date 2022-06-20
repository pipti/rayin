package ink.rayin.app.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.app.web.model.*;
import ink.rayin.app.web.annotation.OrgId;
import ink.rayin.app.web.annotation.UserId;
import ink.rayin.app.web.service.impl.UserElementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

/**
 * [用户构件管理Controller].
 * [用户构件管理 WebApp Rest API,包括构件的增删改查，版本新增，版本查询]
 * <h3>version info：</h3><br>
 * v1.0 2020-02-14 Eric Wang create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
@RestController
public class ElementManagementController {
    @Resource
    private PdfGenerator PdfGenerator;
    @Resource
    UserElementService userElementService;

    /**
     * 构件版本清单
     *
     * @param userId
     * @param pageCurrent
     * @param pageSize
     * @param elementId
     * @return
     */
    @GetMapping(value = "/element/versions/{elementId}")
    public RestResponse elementVersions(@UserId String userId,
                                        @RequestParam Integer pageCurrent, @RequestParam Integer pageSize, @PathVariable(required = true) String elementId) throws Exception {
        UserElementVersionHistory uem = new UserElementVersionHistory();
        uem.setElementId(elementId);
        uem.setUserId(userId);

        Page page = new Page(pageCurrent, pageSize);
        IPage<UserElementVersionHistory> pages = userElementService.userElementVersionList(page, uem);
        return RestResponse.success(pages);
    }

    /**
     * 构件查询
     *
     * @param pageCurrent
     * @param pageSize
     * @param key
     * @return
     */
    @GetMapping(value = {"/element/query/{key}", "/element/query"})
    public RestResponse elementQuery(@OrgId String orgId,
                                     @UserId String userId,
                                     @RequestParam Integer pageCurrent, @RequestParam Integer pageSize,
                                     @PathVariable(required = false) String key,
                                     @RequestParam(required = false) Boolean delFlag) throws Exception {

        UserElement uem = new UserElement();
        uem.setName(key);
        // uem.setUserId(userId);
        uem.setOrganizationId(orgId);
        uem.setDelFlag(delFlag == null?false:delFlag);

        Page page = new Page(pageCurrent, pageSize);
        IPage<UserElement> pages = userElementService.userElementQuery(userId,page, uem);
        return RestResponse.success(pages);
    }


    /**
     * 构件pdf预览
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/element/pdfView", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject elementPdfView(@OrgId String orgId,@RequestBody JSONObject parameter) throws Exception {
        parameter.getJSONObject("data");
        JSONObject r = new JSONObject();
        String html = null;

        try {
            String thtml = parameter.getString("thtml");
            thtml = generateFileUrl(orgId,thtml);
            html = PdfGenerator.htmlStrDataFilling(thtml, parameter.getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (StringUtils.isBlank(html)) {
            html = "<span>无可预览内容</span>";
        }

        final Base64.Encoder encoder = Base64.getEncoder();
        long start = System.currentTimeMillis();
        r.put("filedata", encoder.encodeToString(PdfGenerator.generatePdfStreamByHtmlStr(html).toByteArray()));
        log.debug("生成时间：" + (System.currentTimeMillis() - start)/1000 + "s");
        return r;
    }

    private String generateFileUrl(String orgId, String str) {
        int start;
        int end;
        String target;
        String result;
        do {
            start = str.indexOf("RAYIN://");
            if (start == -1) {
                break;
            }
            end = str.indexOf("\"",start);
            target = str.substring(start,end);
            String[] folderFolders = target.replace("RAYIN://","").split("/");
            if (folderFolders!=null && folderFolders.length > 0) {
//                result = iOrganizationResourceService.getUrl(orgId,folderFolders);
//                str =  str.replaceAll(target,result);
            }
        } while (true);
        return str;
    }
    /**
     * 构件逻辑删除
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/element/logicalDel")
    public RestResponse logicalDel(@UserId String userId,@OrgId String orgId, @RequestBody UserElement parameter) {
        if (userElementService.userElementLogicalDel(parameter,orgId,userId) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 构件彻底删除
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/element/del")
    public RestResponse elementDel(@UserId String userId, @OrgId String orgId,@RequestBody UserElement parameter) {
        if(!userId.equals(parameter.getUserId())){
            throw new RayinBusinessException("只能清理自己创建的构件！");
        }
        if (userElementService.userElementDel(parameter, orgId, userId) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 构件恢复
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/element/resume")
    public RestResponse elementResume(@UserId String userId, @RequestBody UserElement parameter) {
//        if(!userId.equals(parameter.getUserId())){
//            throw new EPrintException("操作的资源与用户不一致，请重新登录！");
//        }
        if (userElementService.userElementResume(parameter) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }

    }


    /**
     * 构件版本恢复
     *
     * @param parameter
     * @return
     */
    @PostMapping(value = "/element/versionResume")
    public RestResponse elementVersionResume(@UserId String userId, @RequestBody UserElementVersionHistory parameter) throws IllegalAccessException, IOException, InstantiationException {

        if (userElementService.userElementVersionResume(userId,parameter) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }

    }

    /**
     * 构件插入、更新、版本新增
     *
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/element/save")
    public RestResponse elementSave(@UserId String userId,
                                    @OrgId String orgId,
                                    @RequestBody UserElement parameter) throws Exception {

        if (StringUtils.isBlank(parameter.getName())) {
            throw new RayinBusinessException("请添加构件名称！");
        }

        if (StringUtils.isBlank(parameter.getContent())) {
            throw new RayinBusinessException("构件没有内容，请编辑构件内容！");
        }
        parameter.setOrganizationId(orgId);
        // 更新操作，但是不覆盖原有记录，生成新的记录，并生成新的版本号
        if (StringUtils.isNotBlank(parameter.getElementId()) &&
                StringUtils.isNotBlank(parameter.getNewFlag()) &&
                parameter.getNewFlag().equals("1")) {
//            if(!userId.equals(parameter.getUserId())){
//                throw new EPrintException("操作的资源与用户不一致，请重新登录！");
//            }
            parameter.setUserId(userId);
            String elementId = userElementService.userElementNewVersionSave(parameter);
            if (StringUtils.isNotBlank(elementId)) {
                return RestResponse.success(elementId);
            } else {
                return RestResponse.failed(BusinessCodeMessage.FAILED);
            }
        }

        // 更新操作，更新原有记录
        if (StringUtils.isNotBlank(parameter.getElementId()) && StringUtils.isBlank(parameter.getNewFlag())) {
            parameter.setUpdateTime(new Date());
            String elementId = userElementService.userElementUpdate(userId, parameter);
            if (StringUtils.isNotBlank(elementId)) {
                return RestResponse.success(elementId);
            } else {
                return RestResponse.failed(BusinessCodeMessage.FAILED);
            }
        }
        parameter.setUserId(userId);
        parameter.setOrganizationId(orgId);
        // 新增操作
        parameter.setCreateTime(new Date());
        String elementId = userElementService.userElementSave(parameter);
        if (StringUtils.isNotBlank(elementId)) {
            return RestResponse.success(elementId);
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }


    /**
     * 构件对应模板关联关系查询
     *
     * @param userId
     * @param orgId
     * @param parameter
     * @param pageCurrent
     * @param pageSize
     * @return
     */

    @PostMapping(value = {"/element/template/relations"})
    public RestResponse elementTemplateRelations(@UserId String userId,
                                                 @OrgId String orgId,
                                                 @RequestBody UserElement parameter,
                                                 @RequestParam Integer pageCurrent, @RequestParam Integer pageSize) throws Exception {

        UserTemplateElement utl = new UserTemplateElement();
        utl.setElementId(parameter.getElementId());
//        utl.setUserId(userId);
       // utl.setElementVersion(parameter.getElementVersion());
        utl.setOrganizationId(orgId);
        Page page = new Page(pageCurrent, pageSize);
        IPage<UserTemplate> pages = userElementService.userElementTemplateRelationsQuery(page, utl);
        return RestResponse.success(pages);
    }


    /**
     * 收藏构件/取消收藏
     *
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/element/colle")
    public RestResponse elementColle(@UserId String userId,
                                     @UserId String orgId,
                                     @RequestBody UserElementFavorites parameter) throws Exception {

        if (StringUtils.isBlank(parameter.getElementId())) {
            throw new RayinBusinessException("您收藏的构件存在错误！");
        }
        parameter.setUserId(userId);

        if (userElementService.userElementColle(parameter,orgId,userId) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 收藏构件预览查询
     *
     * @param pageCurrent
     * @param pageSize
     * @param key
     * @return
     */
    @GetMapping(value = {"/element/favorites/query/{key}", "/element/favorites/query"})
    public RestResponse elementFavoritesQuery(@UserId String userId,
                                              @RequestParam Integer pageCurrent, @RequestParam Integer pageSize, @PathVariable(required = false) String key) throws Exception {

        UserElementFavorites uef = new UserElementFavorites();
        uef.setName(key);
        uef.setUserId(userId);
        Page page = new Page(pageCurrent, pageSize);
        IPage<UserElementFavorites> pages = userElementService.userElementFavoritesQuery(page, uef);
        return RestResponse.success(pages);
    }

    /**
     * 收藏构件删除
     *
     * @param orgId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/element/favorites/del")
    public RestResponse userElementFavoritesDel(@OrgId String orgId, @RequestBody UserElementFavorites parameter) {
//        if(!userId.equals(parameter.getUserId())){
//            throw new EPrintException("操作的资源与用户不一致，请重新登录！");
//        }
        if (userElementService.userElementFavoritesDel(parameter) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 构件模板同步
     * @param userId
     * @param orgId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/element/syncElTl")
    public RestResponse userElementSyncElTl(@UserId String userId,@OrgId String orgId, @RequestBody UserTemplateElement parameter) throws IllegalAccessException, IOException, InstantiationException {
//        if(!userId.equals(parameter.getUserId()) || !orgId.equals(parameter.getOrganizationId())){
//            throw new EPrintException("操作的资源与用户或项目不一致，请重新登录！");
//        }
        if (userElementService.userElementSyncElTl(parameter,userId) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }


    /**
     * 构件模板同步日志查询
     * @param orgId
     * @param pageCurrent
     * @param pageSize
     * @param elementId
     * @return
     */
    @GetMapping(value = "/element/syncElTlLog/query/{elementId}")
    public RestResponse userElementSyncElTlLogQuery(@OrgId String orgId,
                                               @RequestParam Integer pageCurrent, @RequestParam Integer pageSize,
                                               @PathVariable(required = true) String elementId) {
        Page page = new Page(pageCurrent, pageSize);
        IPage<UserElementSyncLog> pages = userElementService.userElementSyncElTlLogQuery(page, orgId, elementId);
        return RestResponse.success(pages);
    }


    /**
     * 构件修改历史查询
     * @param orgId
     * @param pageCurrent
     * @param pageSize
     * @param elementId
     * @return
     */
    @GetMapping(value = "/element/modifyHistory/query/{elementId}")
    public RestResponse userElementModifyHistoryQuery(@OrgId String orgId,
                                                    @RequestParam Integer pageCurrent, @RequestParam Integer pageSize,
                                                    @PathVariable(required = true) String elementId) {
        Page page = new Page(pageCurrent, pageSize);
        IPage<UserElementModifyHistory> pages = userElementService.userElementModifyHistoryQuery(page, orgId, elementId);
        return RestResponse.success(pages);
    }


    /**
     * 构件导入
     *
     * @param orgId
     * @param userId
     * @param parameter
     * @return
     */
    @PostMapping(value = "/element/import")
    public RestResponse userElementImport(@OrgId String orgId, @UserId String userId,
                                          @RequestBody HashMap parameter) throws UnsupportedEncodingException {
        boolean overwriteFlag = (boolean)parameter.get("overwriteFlag");
        String elData = (String)parameter.get("elData");

        if(StringUtils.isBlank(elData) || elData.length() < 34){
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }

        if (userElementService.userElementImport(overwriteFlag, elData, userId, orgId) > 0) {
            return RestResponse.success();
        } else {
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 构件分享
     *
     * @param userId
     * @param elementShare
     * @return
     */
    @PostMapping(value = "/element/user/share")
    public RestResponse userShare(@UserId String userId,
                              @RequestBody ElementShare elementShare){
        if(userElementService.share(elementShare,userId) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 取消构件分享
     *
     * @param userId
     * @param elementShare
     * @return
     */
    @PostMapping(value = "/element/user/cancelShare")
    public RestResponse userCancelShare(@UserId String userId,
                                  @RequestBody ElementShare elementShare){
        if(userElementService.cancelShare(elementShare,userId) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 构件预览
     *
     * @param elementShare
     * @return
     */
    @PostMapping(value = "/element/share/view")
    public RestResponse shareShow(@RequestBody ElementShare elementShare){
        return RestResponse.success(userElementService.sharedView(elementShare));
    }

    /**
     * 构件分享查询
     *
     * @param elementShare
     * @return
     */
    @PostMapping(value = "/element/share/query")
    public RestResponse shareQuery(@RequestBody ElementShare elementShare,
                                   @RequestParam Integer pageCurrent,
                                   @RequestParam Integer pageSize){
        return RestResponse.success(userElementService.shareQuery(elementShare,pageCurrent,pageSize));
    }

    /**
     * 构件收藏
     *
     * @param userId
     * @param elementShare
     * @return
     */
    @PostMapping(value = "/element/share/store")
    public RestResponse memo(@UserId String userId,
                             @RequestBody ElementShare elementShare){
        if(userElementService.copyElement(elementShare,userId)){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }

    }


    /**
     * 构件点赞
     *
     * @param userId
     * @param elementShare
     * @return
     */
    @PostMapping(value = "/element/share/like")
    public RestResponse like(@UserId String userId,
                             @RequestBody ElementShare elementShare){
        if(userElementService.like(elementShare,userId)){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }

    }

    @GetMapping(value = "/element/fonts")
    public RestResponse fonts() throws Exception {
        return RestResponse.success(PdfGenerator.getFontNames());
    }
}