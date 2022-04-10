package ink.rayin.app.web.configuration;

import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.filter.BaseUsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

/**
 *
 * @description: 登录配置类
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
public class LoginConfigurer<T extends LoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B>  {

	private BaseUsernamePasswordAuthenticationFilter authFilter;
	public LoginConfigurer(RedisTemplateUtil redisTemplateUtil) {
		this.authFilter = new BaseUsernamePasswordAuthenticationFilter(redisTemplateUtil);
	}
	@Override
	public void configure(B http) throws Exception {
		LoginFailureHandler loginFailureHandler = new LoginFailureHandler();
		authFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		authFilter.setAuthenticationFailureHandler(loginFailureHandler);
		authFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

		BaseUsernamePasswordAuthenticationFilter filter = postProcess(authFilter);

		http.addFilterAfter(filter, LogoutFilter.class);
	}

	public LoginConfigurer<T,B> loginSuccessHandler(AuthenticationSuccessHandler authSuccessHandler){
		authFilter.setAuthenticationSuccessHandler(authSuccessHandler);
		return this;
	}

}
