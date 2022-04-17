package ink.rayin.app.web.service.impl;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.dao.OrganizationMapper;
import ink.rayin.app.web.dao.UserInfoMapper;
import ink.rayin.app.web.dao.UserMapper;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.exception.UsersServerException;
import ink.rayin.app.web.model.OrganizationModel;
import ink.rayin.app.web.model.UserInfoModel;
import ink.rayin.app.web.model.UserModel;
import ink.rayin.app.web.vo.UserVo;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;




/**
 * @author Jonah wz
 */
@Service
@Slf4j
public class UserServiceImpl implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Setter
    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Setter
    private RedisTemplateUtil redisTemplateUtil;

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private UserInfoMapper userInfoMapper;


    /**
     * 保存用户状态
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    public String[] saveUserLoginInfo(UserDetails user) {
        //状态标志
        String salt = BCrypt.gensalt();
        String username = user.getUsername();
        String key = KeyUtil.makeKey("RAYIN","-",username);
        UserModel userModel = getUserInfoFromCache(username);
        userModel.setSalt(salt);
        redisTemplateUtil.save(key,userModel,Long.valueOf(1800));//用户状态设置半小时后过期
        Algorithm algorithm = Algorithm.HMAC256(userModel.getSalt());
        Date date = new Date(System.currentTimeMillis()+ 3600 * 1000);  //设置token过期时间为1天
        return new String[] {JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId",userModel.getId())
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .sign(algorithm),userModel.getId()};
    }

    public UserServiceImpl(RedisTemplateUtil redisTemplate) {
        //默认使用 bcrypt， strength=10
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.redisTemplateUtil = redisTemplate;
    }


    public UserVo getUserInfo(String userName) {
        UserVo userVo = new UserVo();
        UserModel userModel= getUserInfoFromCache(userName);
        BeanUtils.copyProperties(userModel,userVo);
        List<OrganizationModel> orgs = organizationMapper.selectOrganizationsByUserId(userModel.getId());
        userVo.setOrgs(orgs);
        return userVo;
    }

    @Transactional
    public String createUser(UserModel userModel){
        //TODO 统一返回码
        UserModel user = userMapper.getUserByUsername(userModel.getUsername());
        if (user != null) {
            throw new RayinBusinessException("1002 - 该用户已存在");
        }
        //TODO userId 生成规则
        String id = UUID.randomUUID().toString().replaceAll("-","");
        String encryptPwd = passwordEncoder.encode(userModel.getPassword());
        userModel.setId(id);
        userModel.setPassword(encryptPwd);
        int result = userMapper.insert(userModel);
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUserId(id);
        userInfoModel.setPhone(userModel.getUsername());
        int result2 = userInfoMapper.insert(userInfoModel);
        if (result <= 0 || result2 <= 0) {
            throw new RayinBusinessException("1003 - 用户注册失败");
        }
        return "success";
    }

    @Transactional
    public String createUserByMobile(UserModel userModel) {
        //TODO 统一返回码
        UserModel user = userMapper.getUserByMobile(userModel.getUsername());
        if (user != null) {
            throw new RayinBusinessException("1002 - 该用户已存在");
        }
        //TODO userId 生成规则
        String id = UUID.randomUUID().toString().replaceAll("-","");
        String encryptPwd = passwordEncoder.encode(userModel.getPassword());
        userModel.setId(id);
        userModel.setPassword(encryptPwd);
        userModel.setMobile(userModel.getUsername());
        int result = userMapper.insert(userModel);
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUserId(id);
        userInfoModel.setPhone(userModel.getUsername());
        userInfoModel.setNickName(userModel.getNickName());
        int result2 = userInfoMapper.insert(userInfoModel);
        if (result <= 0 || result2 <= 0) {
            throw new RayinBusinessException("1003 - 用户注册失败");
        }
        return "success";
    }

    /**
     * 加载用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username){
        UserModel userModel = userMapper.getUserByUsername(username);
        if (userModel == null) {
            throw new RayinBusinessException(username + "：用户名/密码错误！请确认输入是否正确！");
        }
        UserDetails userDetails = User.builder().
                username(userModel.getUsername()).
                password(userModel.getPassword()).
                roles(String.valueOf(userModel.getRoleId())).
                build();
        return userDetails;
    }


    /**
     * 清除用户信息
     * @param username
     */
    public void deleteUserLoginInfo(String username) {
        String key = KeyUtil.makeKey("RAYIN","-",username);
        redisTemplateUtil.delete(key);
    }

    /**
     * 获取用户状态
     * @param username
     * @return
     */
    public UserDetails getUserLoginInfo(String username) {

        UserModel userModel = getUserInfoFromCache(username);
        //将salt放到password字段返回
        return User.builder().username(userModel.getUsername()).password(userModel.getSalt()).roles(String.valueOf(userModel.getRoleId())).build();
    }

    /**
     * 加载用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserModel userModel = getUserInfoFromCache(username);
//        UserDetails userDetails = User.builder().username(userModel.getUsername()).password(passwordEncoder.encode(userModel.getPassword())).roles(userModel.getRole()).build();
//        return userDetails;
//    }

    public UserModel getUserInfoFromCache(String username) throws UsernameNotFoundException {
        String key = KeyUtil.makeKey("RAYIN","-",username);

        UserModel userModel = redisTemplateUtil.get(key,UserModel.class);
        if (userModel == null) {
            userModel = getUserInfoFromDb(username);
        }
        if (userModel == null) {
            return null;
        }
        return userModel;
    }

    public UserModel getUserById(String userId) {
        return userMapper.getUserByUserId(userId);
    }
    public UserModel getUserInfoFromDb(String username) throws UsernameNotFoundException {
        UserModel userModel = userMapper.getUserByUsername(username);
        if (userModel == null) {
            throw new RayinBusinessException("未找该用户相关信息！");
        }
        return userModel;
    }


    public UserModel getUserByUserName(String username) {
        UserModel userModel = userMapper.getUserByUsername(username);
        return userModel;
    }


    public String changePassword(String userId, String oldPassword, String newPassword) {
        UserModel userModel = userMapper.getUserByUserId(userId);
        if (userModel == null) {
            return "用户不存在";
        }

        if (StringUtils.isNoneBlank(userModel.getPassword()) && !passwordEncoder.matches(oldPassword,userModel.getPassword())) {
            return "原密码不正确";
        }

        if (!userMapper.updateUserByUserId(new UserModel().setId(userId).setPassword(passwordEncoder.encode(newPassword)))) {
            return "密码修改失败";
        }
        return null;
    }

    /**
     * 第三方获取登录token
     * @param userModel
     * @return
     * @throws UnsupportedEncodingException
     */
    public String ThirdUserLoginInfoWithToken(UserModel userModel) {

        String key = KeyUtil.makeKey("RAYIN","-",userModel.getUsername());
        //UserModel userModelCache = jwtService.getUserInfoFromCache(username);
        UserModel redisUserModel = redisTemplateUtil.get(key,UserModel.class);
        if(redisUserModel == null){
            userModel.setSalt(BCrypt.gensalt());
            redisTemplateUtil.save(key,userModel,Long.valueOf(1800));
        }else{
            userModel = redisUserModel;
        }
        //用户状态设置半小时后过期
        //redisTemplate.updateExpire(key,Long.valueOf(1800));
        Algorithm algorithm = Algorithm.HMAC256(userModel.getSalt());
        logger.debug("token密码盐：" + userModel.getSalt());
        //设置token过期时间为1小时
        Date date = new Date(System.currentTimeMillis() + 3600 * 1000);
        return JWT.create()
                .withSubject(userModel.getUsername())
                .withClaim("userId",userModel.getId())
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }



    public static void main(String[] args){
        logger.debug(passwordEncoder.encode("Qwer4321!"));
    }
}
