package ink.rayin.app.web.controller;

import com.alibaba.fastjson.JSONObject;
import ink.rayin.app.web.model.OrganizationModel;
import ink.rayin.app.web.model.RestResponse;
import ink.rayin.app.web.model.UserModel;
import ink.rayin.app.web.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: rayin-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-02-06 21:23
 **/
@Slf4j
@RestController
public class OrganizationController {
    @Resource
    private OrganizationService organizationService;
    @PostMapping(value = "/users/createOrganization")
    public String createOrg(@RequestBody JSONObject jsonObject) {
        OrganizationModel organizationModel = new OrganizationModel();
        organizationModel.setOrganizationName(jsonObject.getString("organizationName"));
        organizationModel.setOrgNumber(jsonObject.getInteger("orgNumber"));

        organizationService.createOrganization(UserModel.builder().id(jsonObject.getString("userId")).build(),organizationModel);
        System.err.println(organizationModel);
        return "";
    }

    @PostMapping(value = "/users/getMyOrganization")
    public RestResponse getMyOrganization(@RequestBody String userId) {
        return RestResponse.success(organizationService.getMyOrganization(userId));
    }
}
