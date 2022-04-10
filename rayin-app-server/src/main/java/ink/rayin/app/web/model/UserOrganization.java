package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * [user_organization Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-04-01 WangZhu created<br>
 * <br>
 * @author WangZhu
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class UserOrganization implements Serializable{
    private String userId;
    private String organizationId;
    private int roleId;
    private boolean owner;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String organizationName;
    @TableField(exist = false)
    private String icon;
    @TableField(exist = false)
    private String iconColor;
    @TableField(exist = false)
    private String accessKey;
    @TableField(exist = false)
    private String secretKey;
    @TableField(exist = false)
    private String thirdStorageAccessKey;
    @TableField(exist = false)
    private String thirdStorageSecretKey;
    @TableField(exist = false)
    private String thirdStorageBucket;
    @TableField(exist = false)
    private String thirdStorageResourceBucket;
    @TableField(exist = false)
    private String thirdStorageUrl;
    @TableField(exist = false)
    private String ossType;
    @TableField(exist = false)
    private boolean deposit;
    @TableField(exist = false)
    private Date createTime;
    @TableField(exist = false)
    private String createTimeStr;

    public String getCreateTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(createTime != null){
            return dateFormatter.format(createTime);
        }
        return null;
    }
}
