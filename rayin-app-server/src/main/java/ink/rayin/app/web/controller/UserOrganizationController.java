package ink.rayin.app.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.annotation.OrgId;
import ink.rayin.app.web.api.UserOrganizationApi;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.service.impl.UserOrganizationService;
import ink.rayin.app.web.model.*;
import ink.rayin.app.web.annotation.UserId;
import ink.rayin.app.web.service.IOrganizationDataService;
import ink.rayin.app.web.service.IOrganizationIndexesService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * [用户管理Controller].
 * [用户管理 WebApp Rest API]
 * <h3>version info：</h3><br>
 * v1.0 2020-03-31 Eric Wang create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
@RestController
public class UserOrganizationController implements UserOrganizationApi {
    private static Logger logger = LoggerFactory.getLogger(UserOrganizationController.class);

    @Resource
    UserOrganizationService userOrganizationService;

    @Resource
    IOrganizationIndexesService organizationIndexesService;

    @Resource
    IOrganizationDataService organizationDataService;
    /**
     * 项目查询
     *
     * @param pageCurrent
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping(value = {"/organization/query"})
    public RestResponse userOrganizationQuery(@UserId String userId,
                                              @RequestParam Integer pageCurrent, @RequestParam Integer pageSize) throws Exception {

        Page page = new Page(pageCurrent, pageSize);
        IPage<UserOrganization> pages = userOrganizationService.userOrganizationQuery(page, UserOrganization.builder().userId(userId).build());
        return RestResponse.success(pages);
    }


    /**
     * 项目成员查询
     *
     * @param pageCurrent
     * @param pageSize
     * @param orgId
     * @return
     */
    @GetMapping(value = {"/organization/member/query"})
    public RestResponse userOrganizationMemberQuery(@OrgId String orgId,
                                     @RequestParam Integer pageCurrent, @RequestParam Integer pageSize) throws Exception {

        Page page = new Page(pageCurrent, pageSize);
        IPage<UserOrganization> pages = userOrganizationService.userMemberQuery(page, UserOrganization.builder().organizationId(orgId).build());
        return RestResponse.success(pages);
    }

    /**
     * 项目成员查询
     *
     * @param pageCurrent
     * @param pageSize
     * @param orgId
     * @return
     */
    @GetMapping(value = {"/organization/addmember/query/{userinfo}"})
    public RestResponse userOrganizationMemberQuery(@OrgId String orgId,
                                                    @PathVariable(required = true) String userinfo,
                                                    @RequestParam Integer pageCurrent,
                                                    @RequestParam Integer pageSize) throws Exception {

        Users u = new Users();
        u.setUsername(userinfo);

        Page page = new Page(pageCurrent, pageSize);
        IPage<UserOrganization> pages = userOrganizationService.userAddMemberQuery(page, u);
        return RestResponse.success(pages);
    }


    /**
     * 项目成员添加
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/organization/member/add")
    public RestResponse userOrganizationMemberAdd(@UserId String userId,
                                                  @OrgId String orgId,
                                                  @RequestBody Users parameter) throws Exception {

        if(userOrganizationService.userOrganizationMemberAdd(UserOrganization.builder().organizationId(orgId).userId(parameter.getId()).build()) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }


    /**
     * 项目成员添加
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/organization/member/del")
    public RestResponse userOrganizationMemberDel(@UserId String userId, @OrgId String orgId, @RequestBody UserOrganization parameter) throws Exception {

        if(userOrganizationService.userOrganizationMemberDel(parameter,userId,orgId) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 项目移交
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/organization/rightTrans")
    public RestResponse userOrganizationRightTrans(@UserId String userId, @OrgId String orgId, @RequestBody UserOrganization parameter) throws Exception {

        if(userOrganizationService.userOrganizationRightTrans(parameter,userId,orgId) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 项目设置保存
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/organization/save")
    public RestResponse userOrganizationSave(@UserId String userId,
                                                  @OrgId String orgId,
                                                  @RequestBody UserOrganization parameter) throws Exception {
        if(userOrganizationService.userOrganizationSave(parameter,userId,orgId) > 0){
            return RestResponse.success();
        }else{
            return RestResponse.failed(BusinessCodeMessage.FAILED);
        }
    }

    /**
     * 项目彻底删除
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/organization/del")
    public RestResponse userOrganizationDel(@UserId String userId,
                                             @RequestBody UserOrganization parameter) throws Exception {
        userOrganizationService.userOrganizationDel(parameter,userId);

        return RestResponse.success();

    }

    /**
     * 对象存储测试
     * @param parameter
     * @return
     */
    @RequestMapping(value = "/organization/third/storage/test")
    public RestResponse userOrganizationThirdStorageTest(@UserId String userId,
                                             @OrgId String orgId,
                                             @RequestBody UserOrganization parameter) throws Exception {
        try{
            userOrganizationService.userOrganizationThirdStorageTest(parameter,userId,orgId);
            return RestResponse.success(0,"测试成功");
        }catch(Exception e){
            e.printStackTrace();
            return RestResponse.failed(-1,"测试失败！原因：" + e.getMessage());
        }

    }

    /**
     * 项目自定义索引保存
     * @param organizationIndexes
     * @return
     */
    @PostMapping(value = "/organization/index/save")
    public RestResponse userOrganizationIndexSave(@UserId String userId,
                                                         @OrgId String orgId,
                                                         @RequestBody OrganizationIndexes organizationIndexes) throws Exception {
        organizationIndexes.setOrganizationId(orgId);
        organizationIndexes.setUserId(userId);
        organizationIndexesService.organizationIndexSave(organizationIndexes);
        return RestResponse.success();
    }

    /**
     * 项目自定义索引删除
     * @param organizationIndexes
     * @return
     */
//    @PostMapping(value = "/organization/index/del")
//    public RestResponse userOrganizationIndexDel(@UserId String userId,
//                                                 @OrgId String orgId,
//                                                 @RequestBody OrganizationIndexes organizationIndexes) throws Exception {
//        organizationIndexesService.organizationIndexDel(organizationIndexes);
//        return RestResponse.success();
//    }

    /**
     * 项目自定义索引查询
     * @param orgId
     * @param pageCurrent
     * @param pageSize
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/organization/index/query")
    public RestResponse userOrganizationIndexQuery(@OrgId String orgId,
                                                   @RequestParam Integer pageCurrent, @RequestParam Integer pageSize) throws Exception {
        Page page = new Page(pageCurrent, pageSize);

        IPage<OrganizationIndexesUser> pages =  organizationIndexesService.organizationIndexQuery(page,orgId);
        return RestResponse.success(pages);
    }

    @Override
    public List<OrganizationIndexesUser> userOrganizationIndexGetAll(@RequestParam String orgId,
                                                                     @RequestParam Integer pageCurrent, @RequestParam Integer pageSize) throws Exception {
        Page page = new Page(pageCurrent, pageSize);

        IPage<OrganizationIndexesUser> pages =  organizationIndexesService.organizationIndexQuery(page,orgId);
        return pages.getRecords();
    }
}