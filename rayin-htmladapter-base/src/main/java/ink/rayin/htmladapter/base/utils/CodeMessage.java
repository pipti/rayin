package ink.rayin.htmladapter.base.utils;

/**
 * 编码枚举
 * Created by tangyongmao on 2019-6-3.
 */
public enum CodeMessage {
    SUCCESS(0, "SUCCESS"),
    FAILED(-1, "操作失败"),
    //Error code for Http
    HTTP_BAD_REQUEST(400, "请求参数错误"),
    HTTP_UNAUTHORIZED(401, "认证失败"),
    HTTP_DENIED(403,"拒绝访问"),
    HTTP_NOT_FOUND(404, "访问资源不存在"),
    HTTP_INTERNAL_SERVER_ERROR(500, "服务器内部错误，请联系管理员"),
    HTTP_READ_RESP_ERROR(501, "HTTP读取response失败"),
    HTTP_INIT_CLIENT_ERROR(502, "PoolingHttpClientConnectionManager初始化失败"),
    HTTP_FORWARD_SERVER_ERROR(503, "微服务转发错误"),
    DATA_FORMAT_ERROR(1000, "数据格式错误"),
    //Error code for ecs business
    BIZ_UNKNOWN_ERROR(600, "非预期错误"),
    OTHERS(99999, "others error");
    //system code

    private final int code;
    private final String message;

    private CodeMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static CodeMessage fromCode(int code) {
        for(CodeMessage e : CodeMessage.values()){
            if(e.getCode() == code){
                return e;
            }
        }
        return CodeMessage.BIZ_UNKNOWN_ERROR;
    }

    public String getDescribtion() {
        return "{code:"+code+",message:"+message+"}";
    }
}
