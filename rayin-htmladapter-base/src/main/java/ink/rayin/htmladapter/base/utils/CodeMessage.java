/**
 * Copyright (c) 2022-2030, Janah Wang / 王柱 (wangzhu@cityape.tech).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ink.rayin.htmladapter.base.utils;

/**
 * 编码枚举
 * @author Janah Wang / 王柱 2019-6-3
 *
 */
public enum CodeMessage {
    SUCCESS(0, "SUCCESS"),
    FAILED(-1, "opertion failed"),
    //Error code for Http
    HTTP_BAD_REQUEST(400, "Request parameter error"),
    HTTP_UNAUTHORIZED(401, "Authentication failed"),
    HTTP_DENIED(403,"Access denied"),
    HTTP_NOT_FOUND(404, "Access to the resource does not exist"),
    HTTP_INTERNAL_SERVER_ERROR(500, "Server internal error, please contact your administrator"),
    HTTP_READ_RESP_ERROR(501, "HTTP read response failed"),
    HTTP_INIT_CLIENT_ERROR(502, "PoolingHttpClientConnectionManager初始化失败"),
    DATA_FORMAT_ERROR(1000, "data format error"),
    //Error code for ecs business
    BIZ_UNKNOWN_ERROR(600, "unexpected error"),
    DATA_RESOLVE_FAIL(1001, "Data parsing errors"),
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
