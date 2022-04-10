package ink.rayin.app.web.service;

import ink.rayin.app.web.model.UserInfoModel;

/**
 * @program: rayin-parent
 * @description: UserInfoService
 * @author: 作者名字
 * @create: 2020-02-26 10:22
 **/
public interface UserInfoService {
    UserInfoModel getUserInfo(String userId);
    void saveUserInfo(UserInfoModel userInfoModel);
}
