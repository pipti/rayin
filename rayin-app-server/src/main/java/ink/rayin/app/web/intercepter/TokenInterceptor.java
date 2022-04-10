package ink.rayin.app.web.intercepter;

//import com.auth0.jwt.JWT;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import EPrintException;
//import com.ecs.eprint.jwt.configuration.JwtAuthenticationToken;
//import com.ecs.eprint.jwt.service.JwtService;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.spi.ErrorCode;
//import org.slf4j.LoggerFactory;
//import javax.annotation.Resource;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.token.Token;
//import org.springframework.security.core.token.TokenService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor extends HandlerInterceptorAdapter {

//    private JwtService jwtService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

//        String authInfo = request.getHeader("Authorization");
//        String token ;
//        JwtAuthenticationToken authToken;
//        String userId;
//        Authentication authResult = null;
//        if (!StringUtils.isBlank(authInfo)) {
//            token = StringUtils.removeStart(authInfo, "Bearer ");
//            authToken = new JwtAuthenticationToken(JWT.decode(token));
//            userId = authToken.getToken().getClaims().get("userId").asString();
//            if (StringUtils.isBlank(userId)) {
//                throw new EPrintException("认证失败，请重新登录！");
//            }
//
//        }
        return true;
    }



}
