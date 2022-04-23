package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.rayin.app.web.model.UserTemplate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * [MyBatis user_template_approve Dao Mapper].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-02 WangZhu created<br>
 * <br>
 * @version 1.0
 * @since JDK 1.8
 */
@Repository
public interface UserTemplateMapper extends BaseMapper<UserTemplate> {
    // 排除tpl_config字段
    @Select("select user_id,user_name,template_id,template_version,name,memo,create_time,update_time, " +
            "el_config,test_data,start_time,end_time,organization_id,alias," +
            "pdf_start_time,pdf_end_time,password,editable,title,keywords,subject,author,creator,producer,page_num_display_poss " +
            "from user_template " +
            "where organization_id = #{ew.organizationId} and del_flag = #{ew.delFlag} " +
            " and (name like '%${ew.name}%' or memo like '%${ew.name}%' or template_id like '%${ew.name}%'" +
            " or user_name like '%${ew.name}%') order by create_time desc")
    IPage<UserTemplate> selectTemplatePageByName(IPage page, @Param("ew") UserTemplate uem);

    @Select("select user_id,user_name,template_id,template_version,name,memo,create_time,update_time, " +
            "el_config,test_data,start_time,end_time,organization_id, alias," +
            "pdf_start_time,pdf_end_time,password,editable,title,keywords,subject,author,creator,producer,page_num_display_poss " +
            "from user_template " +
            "where organization_id = #{ew.organizationId} and del_flag = #{ew.delFlag} order by create_time desc")
    IPage<UserTemplate> selectTemplatePageNoCondition(IPage page, @Param("ew") UserTemplate uem);
}
