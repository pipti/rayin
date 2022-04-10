package ink.rayin.app.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.dao.OrganizationIndexesMapper;
import ink.rayin.app.web.model.OrganizationIndexes;
import ink.rayin.app.web.model.OrganizationIndexesUser;
import ink.rayin.app.web.service.IOrganizationIndexesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: rayin-app-parent
 * @description: OrganizationIndexesService
 * @author: tangyongmao
 * @create: 2020-06-21 20:58
 **/

@Service
@Slf4j
public class OrganizationIndexesService implements IOrganizationIndexesService {
    @Resource
    private OrganizationIndexesMapper organizationIndexesMapper;
    @Override
    public IPage<OrganizationIndexesUser> organizationIndexQuery(Page page, String organizationId) {
//        QueryWrapper<OrganizationIndexes> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lef
//        queryWrapper.eq("organization_id",organizationId);
        return organizationIndexesMapper.organizationIndexesQuery(page,organizationId);
    }

    @Transactional
    @Override
    public int organizationIndexSave(OrganizationIndexes organizationIndexes) {
        String jsonPath = organizationIndexes.getJsonPath();
        String indexName = jsonPath.substring(jsonPath.lastIndexOf(".") + 1,jsonPath.length());
        organizationIndexes.setIndexName(indexName);
        OrganizationIndexes resOgr = organizationIndexesMapper.selectById(organizationIndexes.getIndexId());
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        if (resOgr == null) {
            organizationIndexes.setCreateDate(dateStr);
        } else {
            organizationIndexes.setUpdateDate(dateStr);
        }
        //项目桶增加索引字段
        List<String> map = new ArrayList<>();
        map.add(indexName);
//        try {
//            String res = cmsClient.createMapping(organizationIndexes.getOrganizationId(),map);
//        } catch (CmsException e) {
//            e.printStackTrace();
//        }
        return resOgr == null ? organizationIndexesMapper.insert(organizationIndexes) : organizationIndexesMapper.updateById(organizationIndexes);
    }

    @Override
    public int organizationIndexDel(OrganizationIndexes organizationIndexes) {
        return organizationIndexesMapper.deleteById(organizationIndexes.getIndexId());
    }
}
