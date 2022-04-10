package ink.rayin.app.web.service.impl;

import ink.rayin.app.web.dao.UserInfoMapper;
import ink.rayin.app.web.dao.UserMapper;
import ink.rayin.app.web.model.UserInfoModel;
import ink.rayin.app.web.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: rayin-parent
 * @description: UserInfoServiceImpl
 * @author: 作者名字
 * @create: 2020-02-26 10:22
 **/
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private UserMapper userMapper;
    @Override
    public UserInfoModel getUserInfo(String userId) {
        //UserModel user = userMapper.getUserByUserId(userId);
        UserInfoModel userInfo = userInfoMapper.getUserByInfoById(userId);
        //userInfo.setUsername(user.getUsername());

        return userInfo;
    }

    @Override
    public void saveUserInfo(UserInfoModel userInfoModel) {
        userInfoMapper.update(userInfoModel);
    }
}
