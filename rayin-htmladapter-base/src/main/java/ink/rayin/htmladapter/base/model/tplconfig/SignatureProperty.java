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
package ink.rayin.htmladapter.base.model.tplconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * 签章签名信息
 *
 * @author Jonah wz 2019-09-08
 */

public class SignatureProperty {
    public SignatureProperty(int x, int y, int pageNum, int zoomPercent, String signatureImage){
        this.x = x;
        this.y = y;
        this.pageNum = pageNum;
        this.signatureImage = signatureImage;
        this.zoomPercent = zoomPercent;
    }

    public SignatureProperty(){

    }
    @Getter
    @Setter
    private int pageNum;


    @Getter
    @Setter
    private int x;

    @Getter
    @Setter
    private int y;

    /**
     * 路径或http、https、base64
     */
    @Getter
    @Setter
    private String signatureImage;

    @Getter
    @Setter
    private int zoomPercent = -50;
}
