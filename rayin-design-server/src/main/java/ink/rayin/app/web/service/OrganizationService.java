package ink.rayin.app.web.service;

import ink.rayin.app.web.model.OrganizationModel;
import ink.rayin.app.web.model.UserModel;

import java.util.List;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: 作者名字
 * @create: 2020-02-06 21:45
 **/
public interface OrganizationService {
    void createOrganization(UserModel userModel, OrganizationModel organizationModel);
    List<OrganizationModel> getMyOrganization(String userId);
}
