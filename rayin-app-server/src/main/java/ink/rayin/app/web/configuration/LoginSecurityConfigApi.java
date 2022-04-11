package ink.rayin.app.web.configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 *
 * @description: 登录配置接口
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
public interface LoginSecurityConfigApi {
    void configure(WebSecurity web) throws Exception ;
    void configure(HttpSecurity http) throws Exception ;
    void configure(AuthenticationManagerBuilder auth) throws Exception;
}
