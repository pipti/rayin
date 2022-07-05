package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * [user_keys Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-09 WangZhu created<br>
 * <br>
 * @author WangZhu
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class UserKeys implements Serializable {

    String userId;
    @TableId(type = IdType.ASSIGN_UUID)
    String accessKey;
    @TableId(type = IdType.ASSIGN_UUID)
    String secretKey;

    Date createTime;
    Date startTime;
    Date endTime;

    @TableField(exist = false)
    String createTimeStr;
    @TableField(exist = false)
    String startTimeStr;
    @TableField(exist = false)
    String endTimeStr;

    public String getCreateTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(createTime != null){
            return dateFormatter.format(createTime);
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
}
