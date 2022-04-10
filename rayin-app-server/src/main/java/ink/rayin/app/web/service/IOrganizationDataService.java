package ink.rayin.app.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.model.OrganizationData;
import ink.rayin.app.web.model.OrganizationDataUser;

import java.util.List;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-06-22 18:35
 **/
public interface IOrganizationDataService {
    IPage<OrganizationDataUser> organizationDataQuery(Page page, String organizationId);
    int organizationDataSave(OrganizationData organizationData);
    int organizationDataDel(List<OrganizationData> organizationData);
}
