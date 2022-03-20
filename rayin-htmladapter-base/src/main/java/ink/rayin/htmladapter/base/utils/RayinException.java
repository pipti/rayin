/**
 * Copyright (c) 2022-2030, Janah wz 王柱 (carefreefly@163.com).
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
 * 业务异常处理
 * Created by tangyongmao on 2019-6-4 11:01:45.
 */
public class RayinException extends RuntimeException {
    private static final long serialVersionUID = 1416428042965937111L;
    private CodeMessage codeMessage;
    private int code;

    public RayinException(String message) {
        this(message,null);
    }

    public RayinException(String message, Exception e) {
        this(CodeMessage.OTHERS, message, e);
    }

    public RayinException(String message, Exception e, Object info) {
        this(String.format("%s info:%s",message,info.toString()), e);
    }

    public RayinException(CodeMessage _code, Exception e) {
        this(_code, _code.getMessage(), e);
    }

    public RayinException(CodeMessage _code, String message, Exception e) {
        super(message, e);
        this.code = _code.getCode();
        this.codeMessage = _code;
    }

    public static RayinException buildBizException(CodeMessage code) {
        RayinException e = new RayinException(code.getMessage());
        e.code = code.getCode();
        e.codeMessage = code;
        return e;
    }

    public static RayinException buildBizException(CodeMessage code, Exception e) {
        RayinException ex = new RayinException(code, e);
        ex.code = code.getCode();
        ex.codeMessage = code;
        return ex;
    }

    public int getCode() {
        return code;
    }

    public CodeMessage getCodeMessage() {
        return codeMessage;
    }
}
