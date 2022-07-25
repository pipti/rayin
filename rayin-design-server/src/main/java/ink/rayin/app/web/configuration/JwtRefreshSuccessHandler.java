package ink.rayin.app.web.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import ink.rayin.app.web.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
/**
 *
 * @description: JWT刷新处理类
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
public class JwtRefreshSuccessHandler implements AuthenticationSuccessHandler{

	//刷新间隔5分钟
	private static final int tokenRefreshInterval = 300;
	
	private JwtService jwtService;
	
	public JwtRefreshSuccessHandler(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		DecodedJWT jwt = ((JwtAuthenticationToken)authentication).getToken();
		boolean shouldRefresh = shouldTokenRefresh(jwt.getIssuedAt());
		if(shouldRefresh) {
            String newToken = jwtService.saveUserLoginInfo((UserDetails)authentication.getPrincipal());
            response.setHeader("Authorization", newToken);
        }	
	}

	/**
	 * 判断是否需要刷新token
	 * @param issueAt
	 * @return
	 */
	protected boolean shouldTokenRefresh(Date issueAt){
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

}
