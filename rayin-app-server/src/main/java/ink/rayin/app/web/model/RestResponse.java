package ink.rayin.app.web.model;



import ink.rayin.app.web.exception.BusinessCodeMessage;

import java.io.Serializable;

/**
 * result实体类
 * Created by tangyongmao on 2019-6-3.
 */
public class RestResponse<T> implements Serializable{
    private int code;
    private String message;
    private T content;

    public static RestResponse<String> success() {
        return success(BusinessCodeMessage.SUCCESS.getCode(), BusinessCodeMessage.SUCCESS.getMessage());
    }

    public static <T> RestResponse<T> success(T content) {
        return success(BusinessCodeMessage.SUCCESS.getCode(),content);
    }

    public static <T> RestResponse<T> success(int code, T content) {
        RestResponse<T> resp = new RestResponse<T>();
        resp.setCode(code);
        resp.setContent(content);
        return resp;
    }

    public static <T> RestResponse<T> success(int code, String message) {
        RestResponse<T> resp = new RestResponse<T>();
        resp.setCode(code);
        resp.setMessage(message);
        return resp;
    }

    public static RestResponse<String> success(BusinessCodeMessage codeMessage) {
        return success(codeMessage.getCode(), codeMessage.getMessage());
    }

    public static RestResponse<String> failed(BusinessCodeMessage codeMessage) {
        return failed(codeMessage.getCode(), codeMessage.getMessage());
    }

    public static RestResponse<String> failed(int code,String message) {
        RestResponse<String> resp = new RestResponse<String>();
        resp.setCode(code);
        resp.setMessage(message);
        return resp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
