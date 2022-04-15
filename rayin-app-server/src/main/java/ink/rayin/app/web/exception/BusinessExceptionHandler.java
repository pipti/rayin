package ink.rayin.app.web.exception;

import ink.rayin.app.web.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Jonah wz
 */
@Slf4j
@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler({RayinBusinessException.class})
    @ResponseBody
    public RestResponse<?> handlerException(RayinBusinessException ex) {
        log.info("[全局业务异常]\r\n业务编码：{}\r\n异常记录：{}",ex.getCode(), ex.getMessage());
        int code = ex.getCode();
        if(ex.getCode() == 0){
            code = -1;
        }
        return RestResponse.failed(code, ex.getMessage());
    }

}
