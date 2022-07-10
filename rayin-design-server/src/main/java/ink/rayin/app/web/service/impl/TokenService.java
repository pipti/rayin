package ink.rayin.app.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qcloud.cos.utils.Base64;
import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.dao.OrganizationMapper;
import ink.rayin.app.web.model.Organization;
import ink.rayin.app.web.model.UserModel;
import ink.rayin.app.web.service.ITokenService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: 作者名字
 * @create: 2020-09-27 11:10
 **/
@Slf4j
@Service
public class TokenService implements ITokenService {
    private ThreadLocal<AtomicInteger> threadLocal = ThreadLocal.withInitial(() -> new AtomicInteger(0));
    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private RedisTemplateUtil redisTemplateUtil;
    @Override
    public String getToken(String accKey, String secretKey){
        try {
            while (true) {
                QueryWrapper<Organization> queryWrapper = new QueryWrapper();
                Organization organization =organizationMapper.selectOne(queryWrapper.lambda().eq(Organization::getAccessKey,accKey));
                if (organization == null) {
                    return null;
                }
                String privateAccKey = organization.getAccessKey();
                String privateSecretKey = organization.getSecretKey();

                if (accKey.equals(privateAccKey) && secretKey.equals(privateSecretKey)) {
                    String key = KeyUtil.makeKey("RAYIN-CREATE","-",accKey);
                    String salt = BCrypt.gensalt();
                    UserModel userModelOld = redisTemplateUtil.get(key,UserModel.class);
                    if (userModelOld == null) {
                        UserModel userModel = new UserModel();
                        userModel.setId(accKey);
                        userModel.setUsername(accKey);
                        userModel.setPassword(secretKey);
                        userModel.setSalt(secretKey + salt);
                        Algorithm algorithm = Algorithm.HMAC256(userModel.getSalt());
                        Date date = new Date(System.currentTimeMillis()+3600 * 1000 * 24 * 7);  //设置token过期时间为1小时
                        String token = JWT.create()
                                .withSubject(userModel.getUsername())
                                .withClaim("userId",userModel.getId())
                                .withExpiresAt(date)
                                .withIssuedAt(new Date())
                                .sign(algorithm);
                        userModel.setToken(token);
                        boolean success = redisTemplateUtil.set(key,userModel);
                        if (success) {
                            return token;
                        }
                        if (threadLocal.get().get() == 10){
                            break;
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            throw e;
                        }
                        threadLocal.get().getAndIncrement();
                    } else {
                        return userModelOld.getToken();
                    }
                }
            }
        } catch (Exception e) {
            threadLocal.get().set(0);
            return null;
        }
        return null;
    }

    @Override
    public UserModel decodeToken(String token) {
        DecodedJWT decodedJWT =  JWT.decode(token);
        String accessKey = decodedJWT.getClaim("userId").asString();
        String key = KeyUtil.makeKey("RAYIN-CREATE","-",accessKey);
        UserModel userModelOld = redisTemplateUtil.get(key,UserModel.class);

       // String secrtyKey = userModelOld.getPassword();
       // if(userModelOld == null){
       // String  secrtyKey = "5400637a1083b0b43289ce84d7041112";
       // }

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userModelOld.getPassword() + userModelOld.getSalt())).build();
        DecodedJWT decodedsJWT = jwtVerifier.verify(token);


        log.debug(JSON.toJSONString(decodedsJWT));
        return userModelOld;
       // log.debug(new String(Base64.decode(decodedJWT.getPayload()),"UTF-8"));
        //JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!34ADAS")).build();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        new TokenService().decodeToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmODQ4NmRmNzMwNzYxYzE3YWI4OTdlYjYzYjRkODhjNyIsImV4cCI6MTY1Nzk2MDU4MywidXNlcklkIjoiZjg0ODZkZjczMDc2MWMxN2FiODk3ZWI2M2I0ZDg4YzciLCJpYXQiOjE2NTczNTU3ODN9.jmnNSOVy9hHaz6ub5G9f4VJJood-gjpEC7-JeZWn0sc");
    }
}
