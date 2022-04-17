package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * [user_element_sync_log Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-24 WangZhu created<br>
 * <br>
 * @author WangZhu
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@Accessors(chain = true)
public class UserElementSyncLog implements Serializable {

    @TableId(type = IdType.ASSIGN_UUID)
    String logId;
    String operUserId;
    String elementId;
    String elementVersion;
    String templateId;
    String templateName;
    String templateVersion;
    String organizationId;
    Date syncTime;
    @TableField(exist = false)
    String syncTimeStr;

    public String getSyncTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(syncTime != null){
            return dateFormatter.format(syncTime);
        }
        return null;
    }
}
