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
 * @description: JWT token验证失败处理类
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
public class JwtLoginFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		//TODO 通过异常判断不同验证失败情况
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

}
