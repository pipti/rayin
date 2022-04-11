package ink.rayin.app.web.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import ink.rayin.app.web.configuration.JwtAuthenticationToken;
import ink.rayin.app.web.model.UserModel;
import ink.rayin.app.web.service.JwtService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.NonceExpiredException;

import java.util.Calendar;

/**
 *
 * @description: JWT toke验证处理
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
public class JwtAuthenticationProvider implements AuthenticationProvider{
	
	private JwtService jwtService;

	public JwtAuthenticationProvider(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		DecodedJWT jwt = ((JwtAuthenticationToken)authentication).getToken();
		if(jwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
			throw new NonceExpiredException("Token expires");
		}
		//前端token中的用户信息
		String username = jwt.getSubject();
		UserDetails user = jwtService.getUserLoginInfo(username);
		if(user == null || user.getPassword()==null) {
			throw new NonceExpiredException("Token expires");
		}
		String encryptSalt = user.getPassword();
		UserModel userModel = jwtService.getUserInfoFromCache(username);
		if(userModel != null && StringUtils.isNotBlank(userModel.getSalt())){
			encryptSalt = userModel.getSalt();
		}
		if(userModel != null && StringUtils.isBlank(userModel.getSalt())){
			userModel.setSalt(encryptSalt);
		}
		try {
            Algorithm algorithm = Algorithm.HMAC256(encryptSalt);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(username)
                    .build();
            verifier.verify(jwt.getToken());
        } catch (Exception e) {
            throw new BadCredentialsException("JWT token verify fail", e);
        }
		JwtAuthenticationToken token = new JwtAuthenticationToken(user, jwt, user.getAuthorities());
		return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(JwtAuthenticationToken.class);
	}

}
