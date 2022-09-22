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

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 单模板属性
 *
 * @author Janah Wang / 王柱 2019-08-25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Element {

    /**
     * 构件路径
     */
    @Getter
    @Setter
    private String elementPath;

    /**
     * 构件路径
     */
    @Getter
    @Setter
    private String elementId;

    /**
     * 构件类型
     * 保留字：catalog
     */
    @Getter
    @Setter
    private String elementType;

    /**
     * 构件版本
     */
    @Getter
    @Setter
    private String elementVersion;

    /**
     * 构件名称
     */
    @Getter
    @Setter
    private String name;

    /**
     * 构件内容
     *
    @Getter
    @Setter
    private String content;

//    @Getter
//    @Setter
//    private PageNumProperties pageNumProperties;
    /**
     * 页码属性
     */
    @Getter
    @Setter
    private boolean pageNumIsFirstPage;

    @Getter
    @Setter
    private boolean pageNumIsCalculate;

    @Getter
    @Setter
    private boolean pageNumIsDisplay;

    @Getter
    @Setter
    private List<PageNumDisplayPos> pageNumDisplayPoss;

    @Getter
    @Setter
    private PageNumStyle pageNumStyle;

    @Getter
    @Setter
    private boolean addBlankPage;

    /**
     * 空白页模板路径
     */
    @Getter
    @Setter
    private String blankElementPath;

    /**
     * 空白页模板内容
     */
    @Getter
    @Setter
    private String blankElementContent;

    /**
     * 页数
     */
    @Getter
    @Setter
    private int pageCount;

    /**
     * 实际页码
     */
    @Getter
    @Setter
    private int pageNum;


    /**
     * 逻辑页码
     */
    @Getter
    @Setter
    private int logicPageNum;

    /**
     * 隐藏标签关键字
     */
    @Getter
    @Setter
    private Set<MarkInfo> markKeys;

    /**
     * 隐藏标签关键字
     */
    @Getter
    @Setter
    private int index;

    /**
     * 构件可用数据显示路径
     * 该参数主要控制构件是否显示，取决数据路径是否存在，如果不存则不显示
     */
    @Getter
    @Setter
    private String elementAvaliableDataPath;

    /**
     * 目录构件内容，html内容
     */
    @Getter
    @Setter
    private String content;

    /**
     * 质检内容
     * overlap 重叠
     * boundary 边界
     * out 超边界
     * garbled 乱码
     * stamp 印章
     */
//    @Getter
//    @Setter
//    private Set<String> qualityTestType;

    /**
     * 构件缺失检查
     */
//    @Getter
//    @Setter
//    private boolean qualityElementHiatusCheck;
}
