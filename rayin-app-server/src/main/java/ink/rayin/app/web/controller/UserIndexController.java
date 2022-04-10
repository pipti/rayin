package ink.rayin.app.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.model.RestResponse;
import ink.rayin.app.web.model.UserIndexes;
import ink.rayin.app.web.model.UserIndexesUser;
import ink.rayin.app.web.annotation.OrgId;
import ink.rayin.app.web.service.IUserIndexesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-06-24 15:11
 **/
@RestController
public class UserIndexController {
    @Resource
    IUserIndexesService iUserIndexesService;

    @GetMapping("/user/index/query/{userId}")
    public RestResponse userIndexQuery(@OrgId String orgId, @PathVariable("userId") String userId, @RequestParam Integer pageCurrent, @RequestParam Integer pageSize) {

        Page page = new Page<>();
        page.setCurrent(pageCurrent);
        page.setSize(pageSize);
        UserIndexes userIndexes = new UserIndexes();
        userIndexes.setOrganizationId(orgId);
        userIndexes.setUserId(userId);
        return RestResponse.success(iUserIndexesService.userIndexQueryWithOrgIndexes(page,userIndexes));
    }

    @PostMapping("/user/index/save/{userId}")
    public RestResponse userIndexSave(@OrgId String orgId,@PathVariable("userId") String userId, @RequestBody List<UserIndexesUser> userIndexesListParams) {
        if (userIndexesListParams != null && userIndexesListParams.size() > 0) {
            List<UserIndexesUser> userIndexesList = new ArrayList<>();
            for (UserIndexesUser userIndexesUser : userIndexesListParams) {
                userIndexesUser.setUserId(userId);
                if (StringUtils.isNotBlank(userIndexesUser.getValue())) {
                    userIndexesList.add(userIndexesUser);
                }
            }
            Page page = new Page<>();
            page.setCurrent(1);
            page.setSize(100);
            UserIndexes userIndexes = new UserIndexes();
            userIndexes.setOrganizationId(orgId);
            userIndexes.setUserId(userId);
            IPage<UserIndexesUser> iPage = iUserIndexesService.userIndexQueryWithOrgIndexes(page,userIndexes);
            List<UserIndexesUser> beforeList = iPage.getRecords();
            List<UserIndexes> delList = new ArrayList<>();
            if (beforeList != null && beforeList.size() > 0) {
                for (UserIndexesUser userIndexesUser : beforeList) {
                    boolean beforeHas = false;
                    for (UserIndexesUser userIndexesUserNew : userIndexesList) {
                        if (userIndexesUser.getIndexId().equals(userIndexesUserNew.getIndexId())) {
                            beforeHas = true;
                        }
                    }
                    if (!beforeHas) {
                        userIndexesUser.setUserId(userId);
                        delList.add(userIndexesUser);
                    }
                }
                for (UserIndexesUser userIndexesUserNew : userIndexesList) {
                    iUserIndexesService.userIndexSave(userIndexesUserNew);
                }
                if (delList != null && delList.size() > 0) {
                    for (UserIndexes userIndexesDel : delList) {
                        int i = iUserIndexesService.userIndexDel(userIndexesDel);
                    }
                }
            } else {
                for (UserIndexesUser userIndexesUserNew : userIndexesList) {
                    iUserIndexesService.userIndexSave(userIndexesUserNew);
                }
            }
        }

        return RestResponse.success();
    }
}
