package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.rayin.app.web.model.OrganizationData;
import ink.rayin.app.web.model.OrganizationDataUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * [MyBatis user_organization Dao Mapper].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-02-21 tangyongmao created<br>
 * <br>
 * @version 1.0
 * @since JDK 1.8
 */
@Repository
public interface OrganizationDataMapper extends BaseMapper<OrganizationData>{
    @Select("select o.*,u.username from organization_data o right join users u on o.user_id = u.id where o.organization_id = #{organizationId}")
    IPage<OrganizationDataUser> organizationDataQueryWithUser(IPage page, @Param("organizationId") String organizationId);
}