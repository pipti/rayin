package ink.rayin.app.web.api;

import ink.rayin.app.web.model.OrganizationIndexesUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserOrganizationApi {
    @GetMapping(value = "/organization/index/getAll")
    List<OrganizationIndexesUser> userOrganizationIndexGetAll(@RequestParam("orgId") String orgId , @RequestParam("pageCurrent") Integer pageCurrent, @RequestParam("pageSize") Integer pageSize) throws Exception;
}
