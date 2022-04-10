package ink.rayin.app.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.model.OrganizationIndexes;
import ink.rayin.app.web.model.UserIndexes;
import ink.rayin.app.web.model.UserIndexesUser;
import ink.rayin.app.web.dao.OrganizationIndexesMapper;
import ink.rayin.app.web.dao.UserIndexesMapper;
import ink.rayin.app.web.service.IUserIndexesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-06-24 15:19
 **/
@Slf4j
@Service
public class UserIndexesService implements IUserIndexesService{

    @Resource
    UserIndexesMapper userIndexesMapper;

    @Resource
    OrganizationIndexesMapper organizationIndexesMapper;
    @Override
    public IPage<UserIndexesUser> userIndexQueryWithOrgIndexes(Page page, UserIndexes userIndexes) {
        QueryWrapper<OrganizationIndexes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("organization_id",userIndexes.getOrganizationId());
        IPage<OrganizationIndexes> orgPage = organizationIndexesMapper.selectPage(page,queryWrapper);
        List<OrganizationIndexes> organizationIndexesList = orgPage.getRecords();
        IPage<UserIndexesUser> uPage = userIndexesMapper.selectPageWithUser(page,userIndexes);
        List<UserIndexesUser> userIndexesUsersList = uPage.getRecords();
        List<UserIndexesUser> newUserIndexList = new ArrayList<>();
        if (organizationIndexesList != null && organizationIndexesList.size() > 0) {
            for (OrganizationIndexes organizationIndexes : organizationIndexesList) {
                UserIndexesUser newUserIndexesUser = new UserIndexesUser();
                for (UserIndexesUser userIndexesUser: userIndexesUsersList) {
                    if (organizationIndexes.getIndexId().equals(userIndexesUser.getIndexId())) {
                        newUserIndexesUser.setValue(userIndexesUser.getValue());
                    }
                }
                newUserIndexesUser.setIndexName(organizationIndexes.getIndexName());
                newUserIndexesUser.setIndexNameChn(organizationIndexes.getIndexNameChn());
                newUserIndexesUser.setIndexId(organizationIndexes.getIndexId());
                newUserIndexesUser.setOrganizationId(organizationIndexes.getOrganizationId());
                newUserIndexesUser.setUserId(organizationIndexes.getUserId());
                newUserIndexList.add(newUserIndexesUser);
            }
        }
        uPage.setRecords(newUserIndexList);
        uPage.setCurrent(orgPage.getCurrent());
        uPage.setPages(orgPage.getPages());
        uPage.setTotal(orgPage.getTotal());
        return uPage;
    }

    @Override
    public IPage<UserIndexesUser> userIndexQuery(Page page, UserIndexes userIndexes) {
        return userIndexesMapper.selectPageWithUser(page,userIndexes);
    }

    @Override
    public int userIndexSave(UserIndexes userIndexes) {
        QueryWrapper<UserIndexes> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userIndexes.getUserId())
                .eq("organization_id",userIndexes.getOrganizationId())
                .eq("index_id",userIndexes.getIndexId());
        List<UserIndexes> list = userIndexesMapper.selectList(queryWrapper);
        if (list != null && list.size() > 0) {
            UpdateWrapper<UserIndexes> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id",userIndexes.getUserId())
                    .eq("organization_id",userIndexes.getOrganizationId())
                    .eq("index_id",userIndexes.getIndexId());
             return userIndexesMapper.update(userIndexes,updateWrapper);
        }
        return userIndexesMapper.insert(userIndexes);
    }

    @Override
    public int userIndexDel(UserIndexes userIndexes) {
        QueryWrapper<UserIndexes> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userIndexes.getUserId())
                .eq("organization_id",userIndexes.getOrganizationId())
                .eq("index_id",userIndexes.getIndexId());
        return userIndexesMapper.delete(queryWrapper);
    }
}
