package ink.rayin.app.web.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: rayin-parent
 * @description: 登出处理类
 * @author: tangyongmao
 * @create: 2020-06-19 12:28
 **/
public class EprintLogoutHandler implements LogoutHandler {

    private RedisTemplateUtil redisTemplateUtil;

    public EprintLogoutHandler (RedisTemplateUtil redisTemplate) {
        this.redisTemplateUtil = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String token = httpServletRequest.getHeader("Authorization");
        DecodedJWT jwt = JWT.decode(token);
        String username = jwt.getSubject();
        String key = KeyUtil.makeKey("RAYIN","-",username);
        redisTemplateUtil.delete(key);
    }
}
