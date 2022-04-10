package ink.rayin.app.web.exception;


/**
 * 业务异常处理
 * Created by tangyongmao on 2019-6-4 11:01:45.
 */
public class UsersServerException extends RuntimeException {
    private static final long serialVersionUID = 1416428042965937111L;
    private BusinessCodeMessage codeMessage;
    private int code;

    public UsersServerException(String message) {
        this(message,null);
    }

    public UsersServerException(String message, Exception e) {
        this(BusinessCodeMessage.OTHERS, message, e);
    }

    public UsersServerException(String message, Exception e, Object info) {
        this(String.format("%s info:%s",message,info.toString()), e);
    }

    public UsersServerException(BusinessCodeMessage _code, Exception e) {
        this(_code, _code.getMessage(), e);
    }

    public UsersServerException(BusinessCodeMessage _code, String message, Exception e) {
        super(message, e);
        this.code = _code.getCode();
        this.codeMessage = _code;
    }

    public static UsersServerException buildBizException(BusinessCodeMessage code) {
        UsersServerException e = new UsersServerException(code.getMessage());
        e.code = code.getCode();
        e.codeMessage = code;
        return e;
    }

    public static UsersServerException buildBizException(BusinessCodeMessage code, Exception e) {
        UsersServerException ex = new UsersServerException(code, e);
        ex.code = code.getCode();
        ex.codeMessage = code;
        return ex;
    }

    public int getCode() {
        return code;
    }

    public BusinessCodeMessage getCodeMessage() {
        return codeMessage;
    }
}
