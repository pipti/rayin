package ink.rayin.app.web.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Aspect
public class OrgIdHandlerArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.getParameterType().isAssignableFrom(String.class) && methodParameter.hasParameterAnnotation(OrgId.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String organizationId = nativeWebRequest.getHeader("organizationId");

//        if (StringUtils.isBlank(organizationId)) {
//            throw new EPrintException("认证失败，请重新登录！");
//        }
        //TODO 鉴权，判断组织代码在该用户名下

        return organizationId;
    }
}
