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

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 自定义源数据
 *
 * @author Jonah wz 2019-08-25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RayinMeta {

    @Getter
    @Setter
    private String filePath;

    @Getter
    @Setter
    private int fileTotalPageCount;

    @Getter
    @Setter
    private String fileSize;

    @Getter
    @Setter
    private String fileMD5;

    @Getter
    @Setter
    private List<Element> pagesInfo;

    @Getter
    @Setter
    private List<MarkInfo> markInfos;

    @Getter
    @Setter
    private String secretKey;
}
