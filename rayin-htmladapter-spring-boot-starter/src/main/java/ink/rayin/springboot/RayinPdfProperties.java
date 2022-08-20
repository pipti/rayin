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
package ink.rayin.springboot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rayin.pdf")
public class RayinPdfProperties {

    /**
     * pdf生成类名
     */
    private String generatorClass;

    /**
     * 字体路径
     */
    private String fontPath;


    /**
     * 最小线程
     */
    int minIdle = 5;
    /**
     * 最大空闲
     */
    int maxIdle = 8;
    /**
     * 最大线程总数
     */
    int maxTotal = 10;

}

