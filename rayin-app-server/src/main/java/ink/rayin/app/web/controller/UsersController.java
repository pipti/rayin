package ink.rayin.app.web.controller;

import com.aliyuncs.utils.StringUtils;
import ink.rayin.app.web.annotation.UserId;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.model.RestResponse;
import ink.rayin.app.web.model.UserInfoModel;
import ink.rayin.app.web.model.UserModel;
import ink.rayin.app.web.service.UserInfoService;
import ink.rayin.app.web.service.impl.UserServiceImpl;
import ink.rayin.app.web.tools.ValidateTools;
import ink.rayin.app.web.vo.UserVo;
import ink.rayin.tools.utils.StringUtil;
import ink.rayin.tools.utils.XmlParseUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
/**
 * @program: rayin-parent
 * @description: 用户操作Controller
 * @author: tym
 * @create: 2020-01-17 11:21
 **/
@Slf4j
@RestController
public class UsersController {
    private static Logger logger = LoggerFactory.getLogger(UsersController.class);
    @Resource
    private RedisTemplateUtil redisTemplateUtil;

    @Resource
    private UserServiceImpl userService;

    @Resource
    private UserInfoService userInfoService;

    @PostMapping(value = "/users/userInfo")
    public RestResponse<UserVo> test(@RequestBody String userName){
        return RestResponse.success(userService.getUserInfo(userName));
    }

    @PostMapping(value = "/users/signup")
    public RestResponse<String> signUp(@RequestBody UserModel userModel, HttpServletRequest request, HttpServletResponse response) {

        String phoneCode = redisTemplateUtil.get("register_sms_" + userModel.getUsername(),String.class);
        if (!StringUtils.isEmpty(phoneCode) && userModel.getVerifyCode().equals(phoneCode)){
            try {
                userService.createUserByMobile(userModel);
                return RestResponse.success();
            } catch (Exception e) {
                return RestResponse.failed(-1,e.getMessage());
            }
        }else {
           return RestResponse.failed(-1,"1001 - 短信验证码无效");
        }
    }

    @PostMapping(value = "/users/saveInfo")
    public RestResponse<String> saveInfo(@RequestBody UserInfoModel userInfoModel) {
        userInfoService.saveUserInfo(userInfoModel);
        return RestResponse.success();
    }

    @GetMapping(value = "/users/userDetails")
    public UserInfoModel getUserInfo(@UserId String userId) {
        return userInfoService.getUserInfo(userId);
    }

    @PostMapping(value = "/users/changePassword")
    public RestResponse<String> changePassword(@UserId String userId, @RequestBody Map<String, String> passwords) {
        if (StringUtils.isEmpty(userId)) {
            throw RayinBusinessException.buildBizException(BusinessCodeMessage.HTTP_UNAUTHORIZED);
        }
        if (StringUtils.isEmpty(passwords.get("newPassword"))) {
            throw new RayinBusinessException("新密码不能为空");
        }
        String ret = userService.changePassword(userId,passwords.get("oldPassword"),passwords.get("newPassword"));
        return StringUtils.isEmpty(ret) ? 
                RestResponse.success() : RestResponse.failed(-1,ret);
    }

    @GetMapping("/users/getSid")
    public RestResponse getSid(@UserId String userId) {
//        UserModel userModel = userService.getUserById(userId);
//        String key = KeyUtil.makeKey("RAYIN","-",userModel.getUsername());
//        userModel = redisTemplate.get(key,UserModel.class);
//        return userModel == null ? RestResponse.failed(-1,"用户不存在"):RestResponse.success(userModel.getSId());
        return RestResponse.success(userId);
    }
}
