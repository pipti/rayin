package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: rayin-app-parent
 * @description: UserInfoModel
 * @author: tangyongmao
 * @create: 2020-02-13 09:51
 **/
@Data
@Accessors(chain = true)
public class UserInfoModel {
    private String userId;
    private String realName;
    private String sex;
    private String birthday;
    private String mail;
    private String weChat;
    private String nickName;
    private String profession;
    private String address;
    private String company;
    private String job;
    private String phone;
    private String headImg;
    private String username;
}
