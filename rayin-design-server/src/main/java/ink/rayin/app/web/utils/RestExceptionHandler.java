package ink.rayin.app.web.utils;

import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.model.RestResponse;
import ink.rayin.htmladapter.base.utils.RayinException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-11-6 10:40:28
 **/
@Slf4j
@ControllerAdvice
public class RestExceptionHandler
{

    @ExceptionHandler({RayinBusinessException.class})
    @ResponseBody
    public ResponseEntity<RestResponse> handleException(RayinBusinessException e) {
        log.error(e.getMessage(),e);
        return new ResponseEntity(RestResponse.failed(e.getCode(), e.getMessage()), HttpStatus.PRECONDITION_REQUIRED);
    }

    @ExceptionHandler({RayinException.class})
    @ResponseBody
    public ResponseEntity<RestResponse> handleException(RayinException e) {
        log.error(e.getMessage(),e);
        return new ResponseEntity(RestResponse.failed(e.getCode(), e.getMessage()), HttpStatus.PRECONDITION_REQUIRED);
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity handleException(Exception e) {
        log.error(e.getMessage(),e);
        return new ResponseEntity(RestResponse.failed(BusinessCodeMessage.OTHERS.getCode(),BusinessCodeMessage.OTHERS.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

