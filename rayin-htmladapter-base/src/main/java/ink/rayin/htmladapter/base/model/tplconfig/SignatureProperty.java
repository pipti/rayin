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

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 签章签名信息
 *
 * @author Jonah wz 2019-09-08
 */

@Data
public class SignatureProperty {
    public SignatureProperty(float x, float y, int pageNum, float width,float height,
                             String signatureImage, String name, String location, String reason, boolean visualSignEnabled){
        this.x = x;
        this.y = y;
        this.pageNum = pageNum;
        this.signatureImage = signatureImage;
        this.width = width;
        this.height = height;
        this.name = name;
        this.location = location;
        this.reason = reason;
        this.visualSignEnabled = visualSignEnabled;
    }

    public SignatureProperty(float x, float y, int pageNum, float width,float height,
                             String signatureImage, String name, String location, String reason){
        this.x = x;
        this.y = y;
        this.pageNum = pageNum;
        this.signatureImage = signatureImage;
        this.width = width;
        this.height = height;
        this.name = name;
        this.location = location;
        this.reason = reason;
    }

    public SignatureProperty(){

    }
    private int pageNum;
    private float x;
    private float y;
    private float width;
    private float height;
    /**
     * 路径或http、https、base64
     */
    private String signatureImage;
    private String name;
    private String location;
    private String reason;
    private boolean visualSignEnabled = true;
}
