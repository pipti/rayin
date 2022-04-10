package ink.rayin.app.web.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @description: 登录失败处理
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
public class LoginFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		//TODO 根据不同异常封装返回码
		response.setStatus(HttpStatus.BAD_GATEWAY.value());
	}

}
