package ink.rayin.app.web.service.impl;

import ink.rayin.app.web.dao.OrganizationMapper;
import ink.rayin.app.web.dao.UserOrganizationMapper;
import ink.rayin.app.web.model.OrganizationModel;
import ink.rayin.app.web.model.UserModel;
import ink.rayin.app.web.model.UserOrganization;
import ink.rayin.app.web.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * @program: rayin-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-02-06 21:46
 **/
@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private UserOrganizationMapper userOrganizationMapper;

    @Transactional(rollbackFor=Exception.class)
    @Override
    public void createOrganization(UserModel userModel, OrganizationModel organizationModel) {
        String orgId = UUID.randomUUID().toString().replaceAll("-","");
        organizationModel.setOrganizationId(orgId);

        userOrganizationMapper.insert(UserOrganization.builder().userId(userModel.getId()).organizationId(organizationModel.getOrganizationId()).build());
        organizationMapper.insert(organizationModel);
    }

    @Override
    public List<OrganizationModel> getMyOrganization(String userId) {
        return organizationMapper.selectOrganizationsByUserId(userId);
    }
}
