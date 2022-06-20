package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ink.rayin.app.web.model.Organization;
import ink.rayin.app.web.model.OrganizationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * [MyBatis user_organization Dao Mapper].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-02-15 WangZhu created<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Repository
public interface OrganizationMapper extends BaseMapper<Organization> {
    int insert(OrganizationModel organizationModel);

    List<OrganizationModel> selectOrganizationsByUserId(String userId);

//    int insertUserOrganization(@Param("userId") String userId, @Param("organizationId") String organizationId);
}
