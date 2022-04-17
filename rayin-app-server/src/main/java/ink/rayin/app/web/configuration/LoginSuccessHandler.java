package ink.rayin.app.web.configuration;

import ink.rayin.app.web.service.impl.UserServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @description: 登录成功处理
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/

public class LoginSuccessHandler implements AuthenticationSuccessHandler{

	private UserServiceImpl userServiceImpl;

	public LoginSuccessHandler(UserServiceImpl userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		String[] res = userServiceImpl.saveUserLoginInfo((UserDetails)authentication.getPrincipal());
		String token = res[0];
		String userId = res[1];
		response.setHeader("Authorization", token);
		PrintWriter writer = response.getWriter();
		writer.append(userId);
		writer.close();
	}

}
