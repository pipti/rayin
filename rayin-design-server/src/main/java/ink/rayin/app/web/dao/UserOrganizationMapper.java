package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.rayin.app.web.model.UserOrganization;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
public interface UserOrganizationMapper extends BaseMapper<UserOrganization> {

    @Select("select b.*,a.username from users a,user_organization b where a.id = b.user_id " +
            "and b.organization_id = #{ew.organizationId} order by b.owner desc,a.username")
    IPage<UserOrganization> userMemberQuery(IPage page, @Param("ew") UserOrganization uo);


    @Select("select a.*,b.* from organization a,user_organization b " +
            "where a.organization_id = b.organization_id " +
            "and b.user_id = #{ew.userId} order by b.seq")
    IPage<UserOrganization>  userOrganizationQuery(IPage page, @Param("ew") UserOrganization uo);


    @Select("select a.*,b.* from organization a,user_organization b " +
            "where a.organization_id = b.organization_id " +
            "and b.user_id = #{ew.userId} and b.organization_id = #{ew.organizationId} order by b.seq")
    UserOrganization  userOrganizationQueryOne(@Param("ew") UserOrganization uo);
}
