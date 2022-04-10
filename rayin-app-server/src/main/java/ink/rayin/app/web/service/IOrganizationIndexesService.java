package ink.rayin.app.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.model.OrganizationIndexes;
import ink.rayin.app.web.model.OrganizationIndexesUser;

public interface IOrganizationIndexesService {
    IPage<OrganizationIndexesUser> organizationIndexQuery(Page page, String organizationId);
    int organizationIndexSave(OrganizationIndexes organizationIndexes);
    int organizationIndexDel(OrganizationIndexes organizationIndexes);
}
