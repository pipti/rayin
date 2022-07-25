package ink.rayin.app.web.configuration;

import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.filter.RayinLogoutHandler;
import ink.rayin.app.web.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 * @description: 登录配置装载
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
@Component
@Configuration
// 这个注解必须加，开启Security
@EnableWebSecurity
//保证post之前的注解可以使用
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private RedisTemplateUtil redisTemplateUtil;

	@Resource
	private UserServiceImpl userService;

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/code/image")
		.antMatchers("/users/code/image");
//		web.ignoring().antMatchers("/index.html", "/static/**", "/login_p", "/favicon.ico");
		// 给 swagger 放行；不需要权限能访问的资源
//		.antMatchers("/swagger-ui.html", "/swagger-resources/**", "/images/**", "/webjars/**", "/v2/api-docs", "/configuration/ui", "/configuration/security");
	}
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//TODO 权限加载
		http.authorizeRequests()
				.antMatchers("/users/signup").permitAll()
				.antMatchers("/users/sendSms").permitAll()
				.antMatchers("/users/getWeChatQRCode").permitAll()
				.antMatchers("/users/webChatLogin").permitAll()
				.antMatchers("/users/wx/callback").permitAll()
				.antMatchers("/users/websocket/*").permitAll()
				.antMatchers("/users/mobile/*").permitAll()
				.antMatchers("/users/login").permitAll()
				.antMatchers("/rayin/**").permitAll()
				//.antMatchers("/**").permitAll()
				.antMatchers("/test1/**").hasAnyRole("ADMIN")
				.antMatchers("/test2/**").hasAnyRole("USER")
				.antMatchers("/test2/**").hasAnyRole("USER")

				//优化
				.antMatchers(HttpMethod.OPTIONS, "/**").anonymous()
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				.formLogin().disable()
				.sessionManagement().disable()
				.cors()
				.and()

				//跨域配置
//				.headers().
//				addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
//				new Header("Access-control-Allow-Origin","*"),
//				new Header("Access-Control-Expose-Headers","Authorization"))))
//				.and()
//				.apply(new JsonLoginConfigurer<>()).loginSuccessHandler(jsonLoginSuccessHandler())
//				.and()
				.logout()
				//默认就是"/logout"
		        .logoutUrl("/users/logout").addLogoutHandler(new RayinLogoutHandler(redisTemplateUtil))
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
				.and()
//				.apply(new ValidateConfigurer<>(loginProperties,redisTemplate)).and()
		    .apply(new LoginConfigurer<>(redisTemplateUtil)).loginSuccessHandler(jsonLoginSuccessHandler())
				.and().sessionManagement().disable();


	}

	@Bean
	protected LoginSuccessHandler jsonLoginSuccessHandler() {
		((UserServiceImpl) userService).setRedisTemplateUtil(redisTemplateUtil);
		return new LoginSuccessHandler((UserServiceImpl) userService);
	}

	@Bean("daoAuthenticationProvider")
	protected AuthenticationProvider daoAuthenticationProvider() throws Exception{
		//这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setHideUserNotFoundExceptions(false);
		daoProvider.setUserDetailsService(userService);
		return daoProvider;
	}

}
