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

import java.util.List;

/**
 * 模板配置
 *
 * @author Janah Wang / 王柱 2019-08-25
 */
public class TemplateConfig {

    @Getter @Setter
    private List<Element> elements;

    @Getter @Setter
    private String templateId;

    /**
     * 别名
     * 主要区别于templateId，不依赖templateId和版本号
     */
    @Getter @Setter
    private String alias;

    @Getter @Setter
    private String templateName;

    @Getter @Setter
    private String blankElementPath;
    @Getter @Setter
    private String blankElementContent;

    /**
     * 主题
     */
    @Getter
    @Setter
    private String subject;

    /**
     * 作者
     */
    @Getter
    @Setter
    private String  author;

    /**
     * 作者
     */
    @Getter
    @Setter
    private String  creator;

    /**
     * 关键字
     */
    @Getter
    @Setter
    private String  keywords;

    /**
     * 标题
     */
    @Getter
    @Setter
    private String  title;

    /**
     * 生产者
     */
    @Getter
    @Setter
    private String  producer;

    /**
     * 密码
     */
    @Getter
    @Setter
    private String password = "";

    /**
     * 生效开始时间
     */
    @Getter
    @Setter
    private String startTime;

    /**
     * 生效结束时间
     */
    @Getter
    @Setter
    private String EndTime;

    /**
     * 是否可编辑
     */
    @Getter
    @Setter
    private boolean editable = true;

    @Getter
    @Setter
    private List<PageNumDisplayPos> pageNumDisplayPoss;


    @Getter
    @Setter
    private List<SignatureProperty> signatureProperties;

}
