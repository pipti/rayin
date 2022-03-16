
package com.rayin.tools.support.xss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Xss配置类
 *
 * @author Chill
 */
@Data
@ConfigurationProperties("blade.xss.url")
public class XssProperties {

	private final List<String> excludePatterns = new ArrayList<>();

}
