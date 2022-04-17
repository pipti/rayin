package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * [user_element Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-02-25 WangZhu created<br>
 * <br>
 * @author WangZhu
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@Accessors(chain = true)
public class UserElement implements Serializable {
    String userId;
    String organizationId;
    @TableId(type = IdType.ASSIGN_UUID)
    String elementId;
    String elementVersion;
    String elementType;
    String name;
    String memo;
    Boolean delFlag;
    String content;
    String testData;
    Date createTime;
    Date updateTime;
    String userName;
    String elementThum;
    boolean shareFlag = false;
    @TableField(exist = false)
    boolean owner = false;
    @TableField(exist = false)
    String createTimeStr;
    String updateUserId;
    String updateUserName;
    @TableField(exist = false)
    String updateTimeStr;
    @TableField(exist = false)
    String newFlag;
    @TableField(exist = false)
    String seq;
    @TableField(exist = false)
    boolean colled;


    public String getElementVersion(){
        if(StringUtils.isBlank(this.elementVersion)){
            return "1.00";
        }else{
            return this.elementVersion;
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
}
