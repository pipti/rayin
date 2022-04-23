package ink.rayin.app.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.rayin.app.web.model.UserTemplateApprove;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * [MyBatis user_template_approve Dao Mapper].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-11-15 WangZhu created<br>
 * <br>
 * @version 1.0
 * @since JDK 1.8
 */
@Repository
public interface UserTemplateApproveMapper extends BaseMapper<UserTemplateApprove> {
    // 排除tpl_config字段
    @Select("select id,user_id,user_name,template_id,template_version,name,memo,create_time,update_time, " +
            "el_config,test_data,start_time,end_time,organization_id,approve_status," +
            "pdf_start_time,pdf_end_time,password,editable,title,keywords,subject,author,creator,producer,submit_user_name,submit_time,new_one " +
            "from user_template_approve " +
            "where to_organization_id = #{ew.toOrganizationId} and approve_status = #{ew.approveStatus}" +
            " and (name like '%${ew.name}%' or memo like '%${ew.name}%' or template_id like '%${ew.name}%'" +
            " or user_name like '%${ew.name}%') order by create_time desc")
    IPage<UserTemplateApprove> selectTemplateApprovePageByName(IPage page, @Param("ew") UserTemplateApprove uea);

    @Select("select id,user_id,user_name,template_id,template_version,name,memo,create_time,update_time, " +
            "el_config,test_data,start_time,end_time,organization_id,approve_status, " +
            "pdf_start_time,pdf_end_time,password,editable,title,keywords,subject,author,creator,producer,submit_user_name,submit_time,new_one " +
            "from user_template_approve " +
            "where to_organization_id = #{ew.toOrganizationId} " +
            "and approve_status = #{ew.approveStatus} order by create_time desc")
    IPage<UserTemplateApprove> selectTemplateApprovePageNoCondition(IPage page, @Param("ew") UserTemplateApprove uea);


    @Select("select id,user_id,user_name,template_id,template_version,name,memo,create_time,update_time, " +
            "el_config,test_data,start_time,end_time,organization_id,approve_status," +
            "pdf_start_time,pdf_end_time,password,editable,title,keywords,subject,author,creator,producer,submit_user_name,submit_time,new_one " +
            "from user_template_approve " +
            "where to_organization_id = #{ew.toOrganizationId} and approve_status != 'wait'" +
            " and (name like '%${ew.name}%' or memo like '%${ew.name}%' or template_id like '%${ew.name}%'" +
            " or user_name like '%${ew.name}%') order by create_time desc")
    IPage<UserTemplateApprove> selectTemplateApproveFinishPageByName(IPage page, @Param("ew") UserTemplateApprove uea);

    @Select("select id,user_id,user_name,template_id,template_version,name,memo,create_time,update_time, " +
            "el_config,test_data,start_time,end_time,organization_id,approve_status, " +
            "pdf_start_time,pdf_end_time,password,editable,title,keywords,subject,author,creator,producer,submit_user_name,submit_time,new_one " +
            "from user_template_approve " +
            "where to_organization_id = #{ew.toOrganizationId} " +
            "and approve_status != 'wait' order by create_time desc")
    IPage<UserTemplateApprove> selectTemplateApproveFinishPageNoCondition(IPage page, @Param("ew") UserTemplateApprove uea);
}
