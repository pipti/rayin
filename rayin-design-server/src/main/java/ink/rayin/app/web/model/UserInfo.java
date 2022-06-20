package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * [user_info Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-04-01 WangZhu created<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@Accessors(chain = true)
public class UserInfo {
    @TableId(type = IdType.ASSIGN_UUID)
    private String userId;
    private String realName;
    private String sex;
    private String birthday;
    private String mail;
    private String wechat;
    private String nickName;
    private String profession;
    private String address;
    private String company;
    private String job;
    private String phone;
    private String headImg;
}
