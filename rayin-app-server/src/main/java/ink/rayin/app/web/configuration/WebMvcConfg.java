package ink.rayin.app.web.configuration;

import ink.rayin.app.web.annotation.OrgIdHandlerArgumentResolver;
import ink.rayin.app.web.annotation.UserIdHandlerArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfg extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
        List<String> includePathPatterns = new ArrayList<String>();
        includePathPatterns.add("/user/**");
        // excludePathPatterns 用户排除拦截
        List<String> excludePathPatterns = new ArrayList<String>();
        // excludePathPatterns.add("");
        //```registry.addInterceptor(getTokenInterceptor()).addPathPatterns(includePathPatterns).excludePathPatterns(excludePathPatterns);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new UserIdHandlerArgumentResolver());
        argumentResolvers.add(new OrgIdHandlerArgumentResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }
}
