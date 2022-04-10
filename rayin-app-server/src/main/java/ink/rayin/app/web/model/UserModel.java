package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: rayin-app-parent
 * @description: UserModel
 * @author: tangyongmao
 * @create: 2020-01-21 17:58
 **/
@Data
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;
    String id;
    String username;
    String password;
    String realName;
    int roleId;
    int organizationId;
    String permission;
    String salt;
    String sessionId;
    String verifyCode;
    String imgCode;
    String thirdId;
    String thirdType;
    String token;
    @TableField(exist = false)
    String wsIp;
    String mobile;
    @TableField(exist = false)
    String nickName;
}