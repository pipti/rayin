package ink.rayin.app.web.model;

import ink.rayin.app.web.configuration.ValidateCodeProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: rayin-app-parent
 * @description: LoginProperties
 * @author: tym
 * @create: 2020-01-16 13:47
 **/
@Data
@Component
@ConfigurationProperties(prefix = "eprint.security")
public class LoginProperties {
    /**
     * 浏览器 属性类
     */
//    private BrowserProperties browser = new BrowserProperties();

    /**
     * 验证码 属性类
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();
}
