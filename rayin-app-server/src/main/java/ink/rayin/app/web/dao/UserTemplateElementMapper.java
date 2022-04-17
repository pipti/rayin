package ink.rayin.app.web.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.model.UserTemplate;
import ink.rayin.app.web.model.UserTemplateElement;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * [MyBatis user_template_element Dao Mapper].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-02 WangZhu created<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Repository
public interface UserTemplateElementMapper extends BaseMapper<UserTemplateElement> {
    @Select("select * from user_template ut JOIN user_template_element ute  ON ut.organization_id= ute.organization_id" +
            " and ut.template_id = ute.template_id and ut.template_version = ute.template_version " +
            "WHERE ut.organization_id= #{ute.organizationId} and ute.element_id = #{ute.elementId} " +
            " and ut.del_flag = #{ute.delFlag}")
    IPage<UserTemplate> userElementTemplateRelationsQuery(Page page, @Param("ute") UserTemplateElement ute);
}

