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
 * @program: rayin-app-parent
 * @description:
 * @author: 作者名字
 * @create: 2020-11-17 16:00
 **/
@Data
public class Message implements Serializable{
    @TableId(type = IdType.ASSIGN_ID)
    private int id;
    private String userId;
    private String info;
    private String url;
    private boolean checked;
    private Date createTime;

    @TableField(exist = false)
    String createTimeStr;

    public String getCreateTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(createTime != null){
            return dateFormatter.format(createTime);
        }
        return null;
    }
}
