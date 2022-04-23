package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * [user_element_version_history Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-02-29 WangZhu created<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@Accessors(chain = true)
public class UserElementVersionHistory implements Serializable {

    String userId;
    String organizationId;
    String elementId;
    String elementVersion;
    String elementType;
    String name;
    String memo;
    Boolean delFlag;
    String content;
    String testData;
    String userName;
    String elementThum;
    Date createTime;
    Date updateTime;
    @TableField(exist = false)
    String createTimeStr;
    @TableField(exist = false)
    String updateTimeStr;
    String updateUserId;
    String updateUserName;

    public UserElementVersionHistory(){
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
}
