package ink.rayin.app.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.model.OrganizationData;
import ink.rayin.app.web.model.OrganizationDataUser;
import ink.rayin.app.web.dao.OrganizationDataMapper;
import ink.rayin.app.web.service.IOrganizationDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-06-22 18:35
 **/
@Slf4j
@Service
public class OrganizationDataService implements IOrganizationDataService {

    @Resource
    private OrganizationDataMapper organizationDataMapper;

    @Override
    public IPage<OrganizationDataUser> organizationDataQuery(Page page, String organizationId) {
        return organizationDataMapper.organizationDataQueryWithUser(page,organizationId);
    }

    @Override
    public int organizationDataSave(OrganizationData organizationData) {
        return organizationDataMapper.insert(organizationData);
    }

    @Transactional
    @Override
    public int organizationDataDel(List<OrganizationData> organizationData) {
        AtomicInteger res = new AtomicInteger();
        organizationData.forEach(o -> {
            QueryWrapper<OrganizationData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("data_id",o.getDataId());
            queryWrapper.eq("user_id",o.getUserId());
            int del = organizationDataMapper.delete(queryWrapper);
            res.addAndGet(del);
        });
        return res.get();
    }
}
