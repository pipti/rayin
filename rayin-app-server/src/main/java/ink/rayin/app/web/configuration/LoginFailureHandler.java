package ink.rayin.app.web.configuration;

import com.google.gson.Gson;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.model.RestResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @description: 登录失败处理
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		//TODO 根据不同异常封装返回码

//		response.setStatus(HttpStatus.UNAUTHORIZED.value());
//		response.se
		Gson gson = new Gson();

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		PrintWriter out = response.getWriter();
		out.write(gson.toJson(RestResponse.failed(HttpStatus.UNAUTHORIZED.value(), exception.getMessage())));
	}

}
