package ink.rayin.app.web.configuration;

import org.springframework.security.core.AuthenticationException;

/**
 * @program: rayin-parent
 * @description: ValidateCodeException
 * @author: tym
 * @create: 2020-01-16 13:34
 **/
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg) {
        super(msg);
    }
}
