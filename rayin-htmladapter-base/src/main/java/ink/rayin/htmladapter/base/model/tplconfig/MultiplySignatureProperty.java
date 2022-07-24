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
package ink.rayin.htmladapter.base.model.tplconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * 签名签章属性
 *
 * 2019-09-08
 * @author Janah Wang / 王柱
 */
public class MultiplySignatureProperty {

    @Getter
    @Setter
    private int startPageNum;

    @Getter
    @Setter
    private int endPageNum;

    @Getter
    @Setter
    private String signatureImagePath;
}
