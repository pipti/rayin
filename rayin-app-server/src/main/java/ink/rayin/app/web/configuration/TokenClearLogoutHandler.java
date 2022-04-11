package ink.rayin.app.web.configuration;

import ink.rayin.app.web.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @description: JWT token清除处理类
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
public class TokenClearLogoutHandler implements LogoutHandler {
	
	private JwtService jwtService;
	
	public TokenClearLogoutHandler(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	/**
	 * 登出
	 * @param request
	 * @param response
	 * @param authentication
	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		clearToken(authentication);
	}
	
	protected void clearToken(Authentication authentication) {
		if(authentication == null) {
			return;
		}
		UserDetails user = (UserDetails)authentication.getPrincipal();
		if(user!=null && user.getUsername()!=null) {
			jwtService.deleteUserLoginInfo(user.getUsername());
		}
	}

}
