package ink.rayin.app.web.configuration;

import ink.rayin.app.web.model.ImageCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @program: rayin-parent
 * @description: ValidateCodeGenerator
 * @author: tym
 * @create: 2020-01-16 13:33
 **/
public interface ValidateCodeGenerator {
    /**
     * 创建验证码
     */
    ImageCode createCode(ServletWebRequest request);
}
