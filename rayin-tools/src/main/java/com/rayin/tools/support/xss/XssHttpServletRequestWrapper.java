
package com.rayin.tools.support.xss;

import com.rayin.tools.utils.Charsets;
import com.rayin.tools.utils.StringUtil;
import com.rayin.tools.utils.WebUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * XSS过滤处理
 *
 * @author Chill
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	/**
	 * 没被包装过的HttpServletRequest（特殊场景,需要自己过滤）
	 */
	HttpServletRequest orgRequest;

	/**
	 * html过滤
	 */
	private final static HtmlFilter HTML_FILTER = new HtmlFilter();

	/**
	 * 缓存报文,支持多次读取流
	 */
	private final byte[] body;

	public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		orgRequest = request;
		body = WebUtil.getRequestBytes(request);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		//为空，直接返回
		if (null == super.getHeader(HttpHeaders.CONTENT_TYPE)) {
			return super.getInputStream();
		}

		//非json类型，直接返回
		if (!super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)
			&& !super.getHeader(HttpHeaders.CONTENT_TYPE).equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
			return super.getInputStream();
		}

		//为空，直接返回
		String requestStr = WebUtil.getRequestStr(orgRequest, body);
		if (StringUtil.isBlank(requestStr)) {
			return super.getInputStream();
		}

		requestStr = xssEncode(requestStr);

		final ByteArrayInputStream bis = new ByteArrayInputStream(requestStr.getBytes(Charsets.UTF_8));

		return new ServletInputStream() {

			@Override
			public boolean isFinished() {
				return true;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}

			@Override
			public int read() throws IOException {
				return bis.read();
			}
		};

	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(xssEncode(name));
		if (StringUtil.isNotBlank(value)) {
			value = xssEncode(value);
		}
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] parameters = super.getParameterValues(name);
		if (parameters == null || parameters.length == 0) {
			return null;
		}

		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = xssEncode(parameters[i]);
		}
		return parameters;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new LinkedHashMap<>();
		Map<String, String[]> parameters = super.getParameterMap();
		for (String key : parameters.keySet()) {
			String[] values = parameters.get(key);
			for (int i = 0; i < values.length; i++) {
				values[i] = xssEncode(values[i]);
			}
			map.put(key, values);
		}
		return map;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(xssEncode(name));
		if (StringUtil.isNotBlank(value)) {
			value = xssEncode(value);
		}
		return value;
	}

	private String xssEncode(String input) {
		return HTML_FILTER.filter(input);
	}

	/**
	 * 获取最原始的request
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}

	/**
	 * 获取最原始的request
	 * @param request request
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
		if (request instanceof XssHttpServletRequestWrapper) {
			return ((XssHttpServletRequestWrapper) request).getOrgRequest();
		}

		return request;
	}

}
