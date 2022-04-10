package ink.rayin.app.web.exception;


/**
 * 业务异常处理
 * Created by tangyongmao on 2019-6-4 11:01:45.
 */
public class RayinBusinessException extends RuntimeException {
    private static final long serialVersionUID = 1416428042965937111L;
    private BusinessCodeMessage codeMessage;
    private int code;

    public RayinBusinessException(String message) {
        this(message,null);
    }

    public RayinBusinessException(String message, Exception e) {
        this(BusinessCodeMessage.OTHERS, message, e);
    }

    public RayinBusinessException(String message, Exception e, Object info) {
        this(String.format("%s info:%s",message,info.toString()), e);
    }

    public RayinBusinessException(BusinessCodeMessage _code, Exception e) {
        this(_code, _code.getMessage(), e);
    }

    public RayinBusinessException(BusinessCodeMessage _code, String message, Exception e) {
        super(message, e);
        this.code = _code.getCode();
        this.codeMessage = _code;
    }

    public static RayinBusinessException buildBizException(BusinessCodeMessage code) {
        RayinBusinessException e = new RayinBusinessException(code.getMessage());
        e.code = code.getCode();
        e.codeMessage = code;
        return e;
    }

    public static RayinBusinessException buildBizException(BusinessCodeMessage code, Exception e) {
        RayinBusinessException ex = new RayinBusinessException(code, e);
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
