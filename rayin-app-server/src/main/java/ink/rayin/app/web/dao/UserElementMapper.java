package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.rayin.app.web.model.UserElement;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * [MyBatis user_element Dao Mapper].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-02-15 WangZhu created<br>
 * <br>
 * @version 1.0
 * @since JDK 1.8
 */
@Repository
public interface UserElementMapper extends BaseMapper<UserElement> {
    @Select("select ut.*,users.username from user_element ue JOIN users  ON ue.user_id = users.id " +
            "WHERE ue.organization_id = #{ew.organizationId} and ue.del_flag = #{ew.delFlag} " +
            " order by ue.create_time desc,ue.update_time desc")
    IPage<UserElement> selectElementPage(IPage page, @Param("ew") UserElement uem);


    @Select("select ut.*,users.username from user_element ue JOIN users  ON ue.user_id = users.id " +
            "WHERE ue.organization_id = #{ew.organizationId} and ue.del_flag = #{ew.delFlag} " +
            " ue.name like '%${ew.name}%' or ue.memo like '%${ew.name}%' " +
            " order by ue.create_time desc, ue.update_time desc")
    IPage<UserElement> selectElementByNamePage(IPage page, @Param("ew") UserElement uem);
}
