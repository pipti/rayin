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

import lombok.Data;
import java.util.List;

/**
 * 模板配置
 *
 * @author Janah Wang / 王柱 2019-08-25
 */
@Data
public class TemplateConfig {

    private List<Element> elements;
    private String templateId;

    /**
     * 别名
     * 主要区别于templateId，不依赖templateId和版本号
     */
    private String alias;
    private String templateName;
    private String blankElementPath;
    private String blankElementContent;
    /**
     * 主题
     */
    private String subject;

    /**
     * 作者
     */
    private String  author;

    /**
     * 关键字
     */
    private String  keywords;

    /**
     * 标题
     */
    private String  title;

    /**
     * 生产者
     */
    private String  producer;

    /**
     * 密码
     */
    private String password = "";

    /**
     * 生效开始时间
     */
    private String startTime;

    /**
     * 生效结束时间
     */
    private String EndTime;

    /**
     * 是否可编辑
     */
    private boolean editable = true;
    /**
     * 模版水印配置
     */
    private WatermarkProperty watermark;
    private List<PageNumDisplayPos> pageNumDisplayPoss;
    private List<SignatureProperty> signatureProperties;

}
