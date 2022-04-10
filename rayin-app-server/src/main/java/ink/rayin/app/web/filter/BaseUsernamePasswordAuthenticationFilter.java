package ink.rayin.app.web.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import ink.rayin.app.web.cache.RedisTemplateUtil;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.exception.UsersServerException;
import ink.rayin.app.web.service.impl.UserServiceImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 * @description: 登录过滤
 *
 * @author: tym
 *
 * @create: 2020-1-14 15:54:35
 **/
public class BaseUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private RedisTemplateUtil redisTemplateUtil;
	@Resource
	UserServiceImpl userService;
	public BaseUsernamePasswordAuthenticationFilter(RedisTemplateUtil redisTemplate) {
		super(new AntPathRequestMatcher("/users/login", "POST"));
		this.redisTemplateUtil = redisTemplate;
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(getAuthenticationManager(), "authenticationManager must be specified");
		Assert.notNull(getSuccessHandler(), "AuthenticationSuccessHandler must be specified");
		Assert.notNull(getFailureHandler(), "AuthenticationFailureHandler must be specified");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {


		String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
		String username = null, password = null, code = null ,loginType = null,smsCode = null;
		if(StringUtils.hasText(body)) {
		    JSONObject jsonObj = JSON.parseObject(body);
		    username = (String) JSONPath.eval(jsonObj,"$.data.username");
		    password = (String) JSONPath.eval(jsonObj,"$.data.password");
			code = (String) JSONPath.eval(jsonObj,"$.data.code");
		}

		validate(code,request.getSession().getId() + "loginPass");
		if (username == null) {
			username = "";
		}
		if (password == null) {
			password = "";
		}
		username = username.trim();
		UsernamePasswordAuthenticationToken userNamePasswordAuthRequest = new UsernamePasswordAuthenticationToken(
				username, password);
		Authentication authentication = null;
		try{
			authentication = this.getAuthenticationManager().authenticate(userNamePasswordAuthRequest);
		}catch(BadCredentialsException e){
			throw new RayinBusinessException("用户认证失败，请确认用户名或密码是否正确");
		}

		return authentication;

	}

	private void validate(String codeInRequest,String sessionId) {
		String key = sessionId;
		//从session中取出 验证码
		String codeInSession = redisTemplateUtil.get(key,String.class);
		//从request 请求中 取出 验证码
		if (org.apache.commons.lang3.StringUtils.isBlank(codeInRequest)){
			logger.info("验证码不能为空");
			throw new UsersServerException("验证码不能为空");
		}
		if (codeInSession == null){
			logger.info("验证码已过期");
			throw new UsersServerException("验证码已过期");
		}
//		if (codeInSession.isExpried()){
//			logger.info("验证码已过期");
//			redisTemplate.delete(key);
//			throw new UsersServerException("验证码已过期");
//		}
		if (!org.apache.commons.lang3.StringUtils.equals(codeInSession,codeInRequest.toLowerCase())){
			logger.info("验证码不匹配"+"codeInSession:"+codeInSession +", codeInRequest:"+codeInRequest);
			throw new UsersServerException("验证码不匹配");
		}
		redisTemplateUtil.delete(key);
		//把对应 的 session信息  删掉
//        sessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY);
	}
}
