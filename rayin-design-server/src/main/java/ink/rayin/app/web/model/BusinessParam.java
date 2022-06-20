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
 * [business_param Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-02 WANGZHU created<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@Accessors(chain = true)
public class BusinessParam implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    String paramId;

    String parentKey;
    String parentId;
    String paramName;
    String paramKey;
    String paramValue;
    String createUserId;
    Date createTime;
    String delFlag;

    @TableField(exist = false)
    String createTimeStr;

    //Date updateTime;

    //@TableField(exist = false)
    //String updateTimeStr;
    //@TableField(exist = false)
    //String newFlag;


    public String getCreateTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(createTime != null){
            return dateFormatter.format(createTime);
        }
        return null;
    }

//    public String getUpdateTimeStr(){
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        if(updateTime != null){
//            return dateFormatter.format(updateTime);
//        }
//        return null;
//    }
}
