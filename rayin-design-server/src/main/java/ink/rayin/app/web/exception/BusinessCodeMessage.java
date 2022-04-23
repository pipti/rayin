package ink.rayin.app.web.exception;

/**
 * 编码枚举
 *
 * @author tangyongmao
 * @date 2019-6-3
 */
public enum BusinessCodeMessage {
    SUCCESS(0, "操作成功"),
    FAILED(-1, "操作失败"),
    //Error code for Http
    HTTP_BAD_REQUEST(400, "请求参数错误"),
    HTTP_UNAUTHORIZED(401, "认证失败"),
    HTTP_AUTHORIZE_DENIED(402, "权限不足"),
    HTTP_DENIED(403,"拒绝访问"),
    HTTP_NOT_FOUND(404, "访问资源不存在"),
    HTTP_INTERNAL_SERVER_ERROR(500, "服务器内部错误，请联系管理员"),
    HTTP_READ_RESP_ERROR(501, "HTTP读取response失败"),
    HTTP_INIT_CLIENT_ERROR(502, "PoolingHttpClientConnectionManager初始化失败"),
    HTTP_FORWARD_SERVER_ERROR(503, "微服务转发错误"),
    //Error code for ecs business
    BIZ_UNKNOWN_ERROR(600, "非预期错误"),
    AUTH_EXPIRE(601, "登录过期，请重新登录"),
    TOKEN_FAILED(602, "TOKEN认证失败"),
    FILE_IS_OK(700,"此文件与开放联盟链上存证匹配，核验为真"),
    FILE_NOT_OUR(701,"此文件不是在本系统生成，无法核验真伪"),
    FILE_NOT_IN_BLOCK_CHAIN(702,"此文件未上开放联盟链"),
    DEPOSIT_NOT_FOUND(703,"开放联盟链上未查询到此文件存证交易"),
    FILE_IS_CHANGED(704,"此文件与开放联盟链上存证不匹配，已被篡改"),
    FILE_NOT_FOUND_CMS(705,"云存储未查询到该文件信息，请联系管理员"),

    DATA_FORMAT_ERROR(1000, "数据格式错误"),
    ACC_KEY_ERROR(1001, "accessKey不可用"),
    OUT_OF_MEMORY(1002, "存储空间不足"),
    TEMPLATE_NOT_FOUND(1003, "对应的模板不存在，请确认模板编号或模板别名，以及版本号是否匹配"),
    TEMPLATE_NOT_RELEASE(1004, "该模板尚未发布，暂时不可用"),
    TEMPLATE_NOT_START(1005, "该模板还未到生效时间"),
    TEMPLATE_TIME_OUT(1006, "该模板已过期"),
    TEMPLATE_OR_ALIAS_NOT_EMPTY_AT_THE_SAME_TIME(1007, "模板编号和别名不能同时为空"),
    OTHERS(99999, "others error");

    private final int code;
    private final String message;

    private BusinessCodeMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static BusinessCodeMessage fromCode(int code) {
        for(BusinessCodeMessage e : BusinessCodeMessage.values()){
            if(e.getCode() == code){
                return e;
            }
        }
        return BusinessCodeMessage.BIZ_UNKNOWN_ERROR;
    }

    public String getDescribtion() {
        return "{code:"+code+",message:"+message+"}";
    }
}
