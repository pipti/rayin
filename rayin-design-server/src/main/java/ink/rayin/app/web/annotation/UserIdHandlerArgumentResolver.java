package ink.rayin.app.web.annotation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Jonah Wang
 */
public class UserIdHandlerArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.getParameterType().isAssignableFrom(String.class) && methodParameter.hasParameterAnnotation(UserId.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String authInfo = nativeWebRequest.getHeader("Authorization");
        String token ;
        DecodedJWT authToken;
        String userId;
        if (!StringUtils.isBlank(authInfo)) {
            token = StringUtils.removeStart(authInfo, "Bearer ");
            authToken = JWT.decode(token);
            userId = authToken.getClaims().get("userId").asString();
//            if (StringUtils.isBlank(userId)) {
//                throw new EPrintException("认证失败，请重新登录！");
//            }
            return userId;
        }

        return null;
    }
}
