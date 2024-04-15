
package ink.rayin.tools.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * 资源工具类
 *
 * @author L.cm
 * modify:2020-08-29 wangzhu
 */
public class ResourceUtil extends org.springframework.util.ResourceUtils {
	public static final String HTTPS_REGEX = "^https?:.+$";
	public static final String HTTP_REGEX = "^http?:.+$";
	public static final String FTP_URL_PREFIX = "ftp:";

	/**
	 * 获取资源
	 * <p>
	 * 支持一下协议：
	 * <p>
	 * 1. classpath:
	 * 2. file:
	 * 3. ftp:
	 * 4. http: and https:
	 * 5. classpath*:
	 * 6. C:/dir1/ and /Users/lcm
	 * </p>
	 *
	 * @param resourceLocation 资源路径
	 * @return {Resource}
	 * @throws IOException IOException
	 */
	public static Resource getResource(String resourceLocation) throws IOException, URISyntaxException {
		Assert.notNull(resourceLocation, "Resource location must not be null");
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return new ClassPathResource(resourceLocation);
		}
		if (resourceLocation.startsWith(FTP_URL_PREFIX)) {
			return new UrlResource(resourceLocation);
		}
		if (resourceLocation.matches(HTTP_REGEX) || (resourceLocation.matches(HTTPS_REGEX))){
			return new UrlResource(resourceLocation);
		}
//		if (resourceLocation.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
//			return SpringUtil.getContext().getResource(resourceLocation);
//		}
		if(!resourceLocation.startsWith(System.getProperty("file.separator")) && resourceLocation.indexOf(":") < 0){
			return new ClassPathResource(resourceLocation);
		}
		if(resourceLocation.indexOf("file:") == 0){
			return new FileSystemResource(new URI(resourceLocation).getPath());
		}

		return new FileSystemResource(resourceLocation);
	}


	/**
	 * 获取资源
	 * <p>
	 * 支持一下协议：
	 * <p>
	 * 1. classpath:
	 * 2. file:
	 * 3. ftp:
	 * 4. http: and https:
	 * 5. classpath*:
	 * 6. C:/dir1/ and /Users/lcm
	 * </p>
	 *
	 * @param resourceLocation 资源路径
	 * @return {InputStream}
	 * @throws IOException IOException
	 */
	public static InputStream getResourceAsStream(String resourceLocation) throws IOException, URISyntaxException {
		return getResource(resourceLocation).getInputStream();
	}

	/**
	 * 获取资源文本
	 * <p>
	 * 支持一下协议：
	 * <p>
	 * 1. classpath:
	 * 2. file:
	 * 3. ftp:
	 * 4. http: and https:
	 * 5. classpath*:
	 * 6. C:/dir1/ and /Users/lcm
	 * </p>
	 *
	 * @param resourceLocation 资源路径
	 * @return {String}
	 * @throws IOException IOException
	 */
	public static String getResourceAsString(final String resourceLocation,final Charset encoding)  {
		if(StringUtil.isBlank(resourceLocation)){
			return "";
		}
		try (InputStream in = getResource(resourceLocation).getInputStream()) {
			return IoUtil.toString(in, encoding);
		} catch (IOException | URISyntaxException e) {
			throw Exceptions.unchecked(e);
		}
	}


	/**
	 * 获取资源文本
	 * <p>
	 * 支持一下协议：
	 * <p>
	 * 1. classpath:
	 * 2. file:
	 * 3. ftp:
	 * 4. http: and https:
	 * 5. classpath*:
	 * 6. C:/dir1/ and /Users/lcm
	 * </p>
	 *
	 * @param resourceLocation 资源路径
	 * @return {ByteArrayOutputStream}
	 * @throws IOException IOException
	 */
	public static ByteArrayOutputStream getResourceAsByte(final String resourceLocation) throws IOException, URISyntaxException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = getResourceAsStream(resourceLocation);

		out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) != -1 ) {
			out.write(buffer, 0, len);
		}
		out.flush();

		return out;
	}

	public static String getResourceAbsolutePathByClassPath(final String classpath)  {
		try{
			return getResource(classpath).getFile().getAbsolutePath();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
