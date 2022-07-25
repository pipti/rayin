package ink.rayin.app.web.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.model.UserModel;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JwtService implements UserDetailsService {

    @Setter
    private static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Setter
    private RedisTemplateUtil redisTemplateUtil;
//    private static Map<String, UserModel> map = new HashMap<>();
//    {
//        UserModel admin = new UserModel();
//        admin.setUsername("admin");
//        admin.setPassword("admin");
//        admin.setRole("ADMIN");
//
//        UserModel test1 = new UserModel();
//        test1.setUsername("test1");
//        test1.setPassword("123456");
//        test1.setRole("USER");
//
//        UserModel test2 = new UserModel();
//        test2.setUsername("test2");
//        test2.setPassword("Qwer4321!");
//        test2.setRole("USER");
//        map.put("admin",admin);
//        map.put("test2",test2);
//        map.put("test1",test1);
//
//    }
    public JwtService() {

    }
    public JwtService(RedisTemplateUtil redisTemplate) {
//        this.passwordEncoder = ;  //默认使用 bcrypt， strength=10
        this.redisTemplateUtil = redisTemplate;
    }

    /**
     * 保存用户状态
     * @param user
     * @return
     *
     */
    public String saveUserLoginInfo(UserDetails user) {
        //状态标志
        String salt = BCrypt.gensalt();
        String username = user.getUsername();
        String key = KeyUtil.makeKey("RAYIN","-",username);
        UserModel userModel = getUserInfoFromCache(username);

        userModel.setSalt(salt);
        redisTemplateUtil.save(key,userModel,Long.valueOf(3600*1000));

        Algorithm algorithm = Algorithm.HMAC256(salt);
        //设置1小时后过期
        Date date = new Date(System.currentTimeMillis()+3600*1000);
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("userId",userModel.getId())
                .withExpiresAt(date)
                .withIssuedAt(new Date())
                .sign(algorithm);
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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = getUserInfoFromCache(username);
        UserDetails userDetails = User.builder().username(userModel.getUsername()).password(userModel.getPassword()).roles(String.valueOf(userModel.getRoleId())).build();
        return userDetails;
    }

    public UserModel getUserInfoFromCache(String username) throws UsernameNotFoundException {
        String key = KeyUtil.makeKey("RAYIN","-",username);

        UserModel userModel = redisTemplateUtil.get(key,UserModel.class);
//        if (userModel == null) {
//            userModel = getUserInfoFromDb(username);
//        }
        if (userModel == null) {
            return null;
        }
        return userModel;
    }

//    public UserModel getUserInfoFromDb(String username) throws UsernameNotFoundException {
//        //TODO DB查找用户信息
//        UserModel userModel = map.get(username);
//        if (userModel == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return userModel;
//    }
}
