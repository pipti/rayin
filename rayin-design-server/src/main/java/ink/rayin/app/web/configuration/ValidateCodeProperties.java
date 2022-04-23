package ink.rayin.app.web.configuration;

import lombok.Data;

/**
 * @program: rayin-parent
 * @description: ValidateCodeProperties
 * @author: tym
 * @create: 2020-01-16 11:05
 **/
@Data
public class ValidateCodeProperties {
    /**
     * 图形验证码 配置属性
     */
    private ImageCodeProperties image = new ImageCodeProperties();
}
