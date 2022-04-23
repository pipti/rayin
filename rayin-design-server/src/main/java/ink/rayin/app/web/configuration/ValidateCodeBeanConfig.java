package ink.rayin.app.web.configuration;

import ink.rayin.app.web.model.LoginProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @program: rayin-parent
 * @description: ValidateCodeBeanConfig
 * @author: tym
 * @create: 2020-01-16 13:32
 **/
@Configuration
public class ValidateCodeBeanConfig {

    @Resource
    private LoginProperties loginProperties;

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    /**
     *
     *
     *
     * 在触发 ValidateCodeGenerator 之前会检测有没有imageCodeGenerator这个bean。
     */
    public ValidateCodeGenerator imageCodeGenerator(){
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setLoginProperties(loginProperties);
        return codeGenerator;
    }

}
