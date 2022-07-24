package ink.rayin.app.web.exception;

import ink.rayin.app.web.model.RestResponse;
import ink.rayin.htmladapter.base.utils.RayinException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Janah Wang / 王柱
 */
@Slf4j
@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler({RayinBusinessException.class})
    @ResponseBody
    public RestResponse<?> handlerException(RayinBusinessException ex) {
        log.info("[全局业务异常]\r\n业务编码：{}\r\n异常记录：{}",ex.getCode(), ex.getMessage());
        int code = ex.getCode();
//        if(ex.getCode() == 0){
//            code = -1;
//        }
        return RestResponse.failed(code, ex.getMessage());
    }

    @ExceptionHandler({RayinException.class})
    @ResponseBody
    public RestResponse<?> handlerException(RayinException ex) {
        log.info("[全局业务异常]\r\n业务编码：{}\r\n异常记录：{}",ex.getCode(), ex.getMessage());
        int code = ex.getCode();
//        if(ex.getCode() == 0){
//            code = -1;
//        }
        return RestResponse.failed(code, ex.getMessage());
    }


    @ExceptionHandler({Exception.class})
    @ResponseBody
    public RestResponse<?> handlerException(Exception ex) {
        log.info("[全局业务异常]\r\n业务编码：{}\r\n异常记录：{}","", ex.getMessage());
        //int code = ex.getCode();
//        if(ex.getCode() == 0){
//            code = -1;
//        }
        return RestResponse.failed(BusinessCodeMessage.OTHERS.getCode(), ex.getMessage());
    }
}
