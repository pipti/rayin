package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * [user_template_approve Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-11-15 WangZhu created<br>
 * <br>
 * @author WangZhu
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class UserTemplateApprove implements Serializable {

    @TableId(type = IdType.ASSIGN_UUID)
    String id;
    String userId;
    String userName;
    String organizationId;
    String templateId;
    String templateVersion;
    String name;
    String alias;
    String memo;
    Boolean delFlag;
    String tplConfig;
    String elConfig;
    String testData;
    String title;
    String keywords;
    String subject;
    String author;
    String creator;
    String producer;
    String password;
    String publicKey;
    boolean editable;

    Date startTime;
    Date endTime;
    Date createTime;
    Date updateTime;
    Date pdfStartTime;
    Date pdfEndTime;
    Boolean templateReleaseStatus;
    String approveStatus;
    String approveIdea;
    String toOrganizationId;
    String submitUserId;
    String submitUserName;
    Date submitTime;
    boolean newOne;

    @TableField(exist = false)
    String createTimeStr;
    @TableField(exist = false)
    String updateTimeStr;
    @TableField(exist = false)
    String startTimeStr;
    @TableField(exist = false)
    String endTimeStr;
    @TableField(exist = false)
    String pdfStartTimeStr;
    @TableField(exist = false)
    String pdfEndTimeStr;
    @TableField(exist = false)
    String newFlag;
    //查询构件模板关系时使用
    @TableField(exist = false)
    String elementVersion;

    @TableField(exist = false)
    String submitTimeStr;

    public String getTemplateVersion(){
        if(StringUtils.isBlank(this.templateVersion)){
            return "1.00";
        }else{
            return this.templateVersion;
        }
    }
    public String getCreateTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(createTime != null){
            return dateFormatter.format(createTime);
        }
        return null;
    }

    public String getUpdateTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(updateTime != null){
            return dateFormatter.format(updateTime);
        }
        return null;
    }

    public String getStartTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(startTime != null){
            return dateFormatter.format(startTime);
        }
        return null;
    }

    public String getEndTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(endTime != null){
            return dateFormatter.format(endTime);
        }
        return null;
    }

    public String getPdfStartTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(pdfStartTime != null){
            return dateFormatter.format(pdfStartTime);
        }
        return null;
    }

    public String getSubmitTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(submitTime != null){
            return dateFormatter.format(submitTime);
        }
        return null;
    }

    public String getStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(pdfEndTime != null){
            return dateFormatter.format(pdfEndTime);
        }
        return null;
    }
}