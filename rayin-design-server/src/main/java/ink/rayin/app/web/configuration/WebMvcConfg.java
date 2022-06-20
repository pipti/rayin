package ink.rayin.app.web.configuration;

import ink.rayin.app.web.annotation.OrgIdHandlerArgumentResolver;
import ink.rayin.app.web.annotation.UserIdHandlerArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new UserIdHandlerArgumentResolver());
        argumentResolvers.add(new OrgIdHandlerArgumentResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

    /**
     * 添加主页方法
     *
     * @param registry 主页注册器
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //设置后台管理模块主页
        registry.addViewController("/rayin").setViewName("/static/index");
        registry.addViewController("/static").setViewName("/static");
        //设置前台页面模块主页
        registry.addViewController("/").setViewName("/static/index");
        //设置优先级
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        //将主页注册器添加到视图控制器中
        super.addViewControllers(registry);
    }
}
