package ink.rayin.app.web.service.impl;

import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.dao.UserMapper;
import ink.rayin.app.web.exception.UsersServerException;
import ink.rayin.app.web.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class UserMoblieServiceImpl implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(UserMoblieServiceImpl.class);

//    @Setter
//    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//
//    @Setter
//    private RedisTemplateUtil redisTemplateUtil;

    @Resource
    private UserMapper userMapper;

//    @Resource
//    private OrganizationMapper organizationMapper;
//
//    @Resource
//    private UserInfoMapper userInfoMapper;
//
//    @Resource
//    private WxProcessInTextMsg wxProcessInTextMsg;
//    @Resource
//    private WxProcessInImageMsg wxProcessInImageMsg;
//    @Resource
//    private WxProcessInVoiceMsg wxProcessInVoiceMsg;
//    @Resource
//    private WxProcessInVideoMsg wxProcessInVideoMsg;
//    @Resource
//    private WxProcessInShortVideoMsg wxProcessInShortVideoMsg;
//    @Resource
//    private WxProcessInLocationMsg wxProcessInLocationMsg;
//    @Resource
//    private WxProcessInLinkMsg wxProcessInLinkMsg;
//    @Resource
//    private WxProcessEvent wxProcessEvent;
//    @Resource
//    private WxProcessDefault wxProcessDefault;
//    @Resource
//    private WxConfigService wxConfigService;

//    /**
//     * 保存用户状态
//     * @param user
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    public String[] saveUserLoginInfo(UserDetails user) throws UnsupportedEncodingException {
//        //状态标志
//        String salt = BCrypt.gensalt();
//        String username = user.getUsername();
//        String key = KeyUtil.makeKey("RAYIN","-",username);
//        UserModel userModel = getUserInfoFromCache(username);
//        userModel.setSalt(salt);
//        redisTemplate.save(key,userModel,Long.valueOf(1800));//用户状态设置半小时后过期
//        Algorithm algorithm = Algorithm.HMAC256(userModel.getSalt());
//        Date date = new Date(System.currentTimeMillis()+ 3600 * 1000);  //设置token过期时间为1天
//        return new String[] {JWT.create()
//                .withSubject(user.getUsername())
//                .withClaim("userId",userModel.getId())
//                .withExpiresAt(date)
//                .withIssuedAt(new Date())
//                .sign(algorithm),userModel.getId()};
//    }

    public UserMoblieServiceImpl(RedisTemplateUtil redisTemplate) {
//        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();  //默认使用 bcrypt， strength=10
//        this.redisTemplate = redisTemplate;
    }


//    public UserVo getUserInfo(String userName) {
//        UserVo userVo = new UserVo();
//        UserModel userModel= getUserInfoFromCache(userName);
//        BeanUtils.copyProperties(userModel,userVo);
//        List<OrganizationModel> orgs = organizationMapper.selectOrganizationsByUserId(userModel.getId());
//        userVo.setOrgs(orgs);
//        return userVo;
//    }

//    @Transactional
//    public String createUser(UserModel userModel) throws Exception{
//        //TODO 统一返回码
//        UserModel user = userMapper.getUserByUsername(userModel.getUsername());
//        if (user != null) {
//            throw new Exception("1002 - 该用户已存在");
//        }
//        //TODO userId 生成规则
//        String id = UUID.randomUUID().toString().replaceAll("-","");
//        String encryptPwd = passwordEncoder.encode(userModel.getPassword());
//        userModel.setId(id);
//        userModel.setPassword(encryptPwd);
//        int result = userMapper.insert(userModel);
//        UserInfoModel userInfoModel = new UserInfoModel();
//        userInfoModel.setUserId(id);
//        userInfoModel.setPhone(userModel.getUsername());
//        int result2 = userInfoMapper.insert(userInfoModel);
//        if (result <= 0 || result2 <= 0) {
//            throw new Exception("1003 - 用户注册失败");
//        }
//        return "success";
//    }

    /**
     * 加载用户信息
     * @param moblie
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String moblie) throws UsernameNotFoundException {
        UserModel userModel = userMapper.getUserByMobile(moblie);
        if (userModel == null) {
            throw new UsersServerException(moblie + "：手机号不存在！");
        }
        UserDetails userDetails = User.builder().
                username(userModel.getUsername()).
                password(userModel.getPassword()).
                roles(String.valueOf(userModel.getRoleId())).
                build();
        return userDetails;
    }


//    /**
//     * 清除用户信息
//     * @param username
//     */
//    public void deleteUserLoginInfo(String username) {
//        String key = KeyUtil.makeKey("RAYIN","-",username);
//        redisTemplate.delete(key);
//    }
//
//    /**
//     * 获取用户状态
//     * @param username
//     * @return
//     */
//    public UserDetails getUserLoginInfo(String username) {
//
//        UserModel userModel = getUserInfoFromCache(username);
//        //将salt放到password字段返回
//        return User.builder().username(userModel.getUsername()).password(userModel.getSalt()).roles(String.valueOf(userModel.getRoleId())).build();
//    }
//
//    /**
//     * 加载用户信息
//     * @param username
//     * @return
//     * @throws UsernameNotFoundException
//     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserModel userModel = getUserInfoFromCache(username);
//        UserDetails userDetails = User.builder().username(userModel.getUsername()).password(passwordEncoder.encode(userModel.getPassword())).roles(userModel.getRole()).build();
//        return userDetails;
//    }

//    public UserModel getUserInfoFromCache(String username) throws UsernameNotFoundException {
//        String key = KeyUtil.makeKey("RAYIN","-",username);
//
//        UserModel userModel = redisTemplate.get(key,UserModel.class);
//        if (userModel == null) {
//            userModel = getUserInfoFromDb(username);
//        }
//        if (userModel == null) {
//            return null;
//        }
//        return userModel;
//    }
//
//    public UserModel getUserById(String userId) {
//        return userMapper.getUserByUserId(userId);
//    }
//    public UserModel getUserInfoFromDb(String username) throws UsernameNotFoundException {
//        UserModel userModel = userMapper.getUserByUsername(username);
//        if (userModel == null) {
//            throw new UsersServerException("未找该用户相关信息！");
//        }
//        return userModel;
//    }
//
//
//    public UserModel getUserByUserName(String username) {
//        UserModel userModel = userMapper.getUserByUsername(username);
//        return userModel;
//    }
//
//
//    public boolean sendSms(String phoneNumbers, Map<String, Object> code) throws Exception{
//        try {
//            boolean isSuccess = SendSms.sendSms(phoneNumbers, code);
//            return isSuccess;
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//
//    }
//
//    public String changePassword(String userId, String oldPassword, String newPassword) {
//        UserModel userModel = userMapper.getUserByUserId(userId);
//        if (userModel == null) {
//            return "用户不存在";
//        }
//
//        if (StringUtils.isNoneBlank(userModel.getPassword()) && !passwordEncoder.matches(oldPassword,userModel.getPassword())) {
//            return "原密码不正确";
//        }
//
//        UserModel user = new UserModel();
//        user.setId(userId);
//        user.setPassword(passwordEncoder.encode(newPassword));
//        if (!userMapper.updateUserByUserId(user)) {
//            return "密码修改失败";
//        }
//        return null;
//    }
//
//    public String getWeChatQRCode(int sceneId) throws Exception{
//        String result = "";
//        try {
//            String access_token = wxConfigService.getWxConfig().getAccessToken();
//                   // WeChat.getPubAccessToken(wxConfigService.getWxConfig().getWxAppid(),wxConfigService.getWxConfig().getWxAppsecret(),redisTemplate);
//            result = WeChat.createTempTicket(access_token,"30",123,1);
//            return result;
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//
//    }
//
//    public String WeChatLogin(String code) throws Exception{
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = WeChat.getOpenId(code,wxConfigService.getWxConfig().getWxAppid());
//            String openid = jsonObject.getString("openid");
//            String access_token = jsonObject.getString("access_token");
//            if (!StringUtils.isEmpty(openid) && !StringUtils.isEmpty(access_token)){
//                jsonObject = WeChat.getUserInfo(access_token,openid);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return jsonObject.toString();
//    }
//
//
//    public OutMsg wxCallbackPost(String msgType, InMsg model) throws Exception {
//
//        if(WxConfig.getMsgTypeId(msgType) == 8
//                && (model.getEvent().equals("SCAN")
//                || model.getEvent().equals("subscribe"))){
//
//                JSONObject jsonObject = WxJSUtil.getUserInfo(wxConfigService.getWxConfig().getAccessToken(),model.getFromUserName());
//                logger.debug("微信登录返回：" + jsonObject.toJSONString());
//                if(StringUtils.isNotBlank(jsonObject.getString("errcode"))){
//                    OutMsg errOutMsg = OutMsg.getToMsg(model);
//                    errOutMsg.setContent(jsonObject.getString("errcode"));
//                    return errOutMsg;
//                }
//                WXUserInfo wxUserInfo = JSONObject.toJavaObject(jsonObject,WXUserInfo.class);
//
//                UserModel userModel = userMapper.getUserByThirdId("wx",wxUserInfo.getOpenid());
//            if(userModel == null){
//                userModel = new UserModel();
//
//                //TODO userId 生成规则
//                String id = UUID.randomUUID().toString().replaceAll("-","");
//                userModel.setId(id);
//                //userModel.setUsername();
//                userModel.setThirdId(wxUserInfo.getOpenid());
//                userModel.setThirdType("wx");
//                userModel.setUsername(wxUserInfo.getOpenid());
//                int result = userMapper.insert(userModel);
//
//                UserInfoModel userInfoModel = new UserInfoModel();
//                userInfoModel.setUserId(id);
//                userInfoModel.setSex(wxUserInfo.getSex());
//                userInfoModel.setWeChat(wxUserInfo.getNickname());
//                userInfoModel.setUsername(wxUserInfo.getOpenid());
//                userInfoModel.setNickName(wxUserInfo.getNickname());
//                userInfoModel.setHeadImg("data:image/png;base64," + Base64Util.encodeToString(ResourceUtil.getResourceAsByte(wxUserInfo.getHeadimgurl()).toByteArray()));
//                int result2 = userInfoMapper.insert(userInfoModel);
//
//            }
//
//            HashMap authRes = new HashMap();
//            authRes.put("authorization",ThirdUserLoginInfoWithToken(userModel));
//            authRes.put("username",userModel.getUsername());
//            authRes.put("userId",userModel.getId());
//            RestResponse<HashMap> restResponse = new RestResponse<HashMap>();
//            restResponse.setContent(authRes);
//            userModel.setId(model.getTicket());
//
//
//            String key = KeyUtil.makeKey("RAYIN", "-", model.getTicket());
//            UserModel userModelCache = redisTemplate.get(key,UserModel.class);
//            if(userModel != null){
//                userModel.setWsIp(userModelCache.getWsIp());
//                redisTemplate.delete(key);
//            }
//
//            WebSocketServer.sendInfo(JSONObject.toJSONString(restResponse),userModel);
//        }
//
//
//        /**
//         * 消息类型
//         * 1-text:文本消息
//         * 2-image：图片消息
//         * 3-voice：语音消息
//         * 4-video:视频
//         * 5-shortvideo：小视频
//         * 6-location:地址位置消息
//         * 7-link:链接消息
//         * 8-event:事件
//         */
//        switch(WxConfig.getMsgTypeId(msgType)){
//            case 1:
//                return wxProcessInTextMsg.process(model);
//            case 2:
//                return wxProcessInImageMsg.process(model);
//            case 3:
//                return wxProcessInVoiceMsg.process(model);
//            case 4:
//                return wxProcessInVideoMsg.process(model);
//            case 5:
//                return wxProcessInShortVideoMsg.process(model);
//            case 6:
//                return wxProcessInLocationMsg.process(model);
//            case 7:
//                return wxProcessInLinkMsg.process(model);
//            case 8:
//                return wxProcessEvent.process(model);
//            default:
//                return wxProcessDefault.process(model);
//        }
//
//
//    }
//
//    /**
//     * 第三方获取登录token
//     * @param userModel
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    public String ThirdUserLoginInfoWithToken(UserModel userModel) throws UnsupportedEncodingException {
//
//        String key = KeyUtil.makeKey("RAYIN","-",userModel.getUsername());
//        //UserModel userModelCache = jwtService.getUserInfoFromCache(username);
//        UserModel redisUserModel = redisTemplate.get(key,UserModel.class);
//        if(redisUserModel == null){
//            userModel.setSalt(BCrypt.gensalt());
//            redisTemplate.save(key,userModel,Long.valueOf(1800));
//        }else{
//            userModel = redisUserModel;
//        }
//        //redisTemplate.updateExpire(key,Long.valueOf(1800));//用户状态设置半小时后过期
//        Algorithm algorithm = Algorithm.HMAC256(userModel.getSalt());
//        logger.debug("token密码盐：" + userModel.getSalt());
//        Date date = new Date(System.currentTimeMillis() + 3600 * 1000);  //设置token过期时间为1小时
//        return JWT.create()
//                .withSubject(userModel.getUsername())
//                .withClaim("userId",userModel.getId())
//                .withExpiresAt(date)
//                .withIssuedAt(new Date())
//                .sign(algorithm);
//    }
//
//    public boolean checkSignature(String signature, String timestamp, String nonce) throws Exception {
//        String token = wxConfigService.getWxConfig().getToken();
//
//        String[] ArrTmp = { token, timestamp, nonce };
//        Arrays.sort(ArrTmp);
//
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < ArrTmp.length; i++) {
//            sb.append(ArrTmp[i]);
//        }
//
//        String signaturecheck = Func.sha1(sb.toString());
//        if(signature.equals(signaturecheck)){
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//
//
//    public static void main(String[] args){
//        logger.debug(passwordEncoder.encode("Qwer4321!"));
//    }
}
