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

    private String elementPath;

    /**
     * 构件路径
     */

    private String elementId;

    /**
     * 构件类型
     * 保留字：catalog
     */

    private String elementType;

    /**
     * 构件版本
     */

    private String elementVersion;

    /**
     * 构件名称
     */

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
    private boolean pageNumIsFirstPage;

    private boolean pageNumIsCalculate;

    private boolean pageNumIsDisplay;

    private List<PageNumDisplayPos> pageNumDisplayPoss;

    private PageNumStyle pageNumStyle;

    private boolean addBlankPage;

    /**
     * 空白页模板路径
     */
    private String blankElementPath;

    /**
     * 空白页模板内容
     */
    private String blankElementContent;

    /**
     * 页数
     */
    private int pageCount;

    /**
     * 实际页码
     */
    private int pageNum;

    /**
     * 逻辑页码
     */
    private int logicPageNum;

    /**
     * 隐藏标签关键字
     */
    private Set<MarkInfo> markKeys;

    /**
     * 隐藏标签关键字
     */
    private int index;

    /**
     * 构件可用数据显示路径
     * 该参数主要控制构件是否显示，取决数据路径是否存在，如果不存则不显示
     */
    private String elementAvaliableDataPath;
    /**
     * 目录构件内容，html内容
     */
    private String content;

    /**
     * header 构件路径 chromium组件有效
     */
    private String marginPath;
    /**
     * footer 构件路径 chromium组件有效
     */
    private String footerPath;
    /**
     * header html chromium组件有效
     */
    private String marginContent;
    /**
     * footer html chromium组件有效
     */
    private String footerContent;

    /**
     * 支持js解析模式 chromium组件有效,
     */
    private boolean jsMode = false;

    public String getElementPath() {
        return elementPath;
    }

    public void setElementPath(String elementPath) {
        this.elementPath = elementPath;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getElementVersion() {
        return elementVersion;
    }

    public void setElementVersion(String elementVersion) {
        this.elementVersion = elementVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPageNumIsFirstPage() {
        return pageNumIsFirstPage;
    }

    public void setPageNumIsFirstPage(boolean pageNumIsFirstPage) {
        this.pageNumIsFirstPage = pageNumIsFirstPage;
    }

    public boolean isPageNumIsCalculate() {
        return pageNumIsCalculate;
    }

    public void setPageNumIsCalculate(boolean pageNumIsCalculate) {
        this.pageNumIsCalculate = pageNumIsCalculate;
    }

    public boolean isPageNumIsDisplay() {
        return pageNumIsDisplay;
    }

    public void setPageNumIsDisplay(boolean pageNumIsDisplay) {
        this.pageNumIsDisplay = pageNumIsDisplay;
    }

    public List<PageNumDisplayPos> getPageNumDisplayPoss() {
        return pageNumDisplayPoss;
    }

    public void setPageNumDisplayPoss(List<PageNumDisplayPos> pageNumDisplayPoss) {
        this.pageNumDisplayPoss = pageNumDisplayPoss;
    }

    public PageNumStyle getPageNumStyle() {
        return pageNumStyle;
    }

    public void setPageNumStyle(PageNumStyle pageNumStyle) {
        this.pageNumStyle = pageNumStyle;
    }

    public boolean isAddBlankPage() {
        return addBlankPage;
    }

    public void setAddBlankPage(boolean addBlankPage) {
        this.addBlankPage = addBlankPage;
    }

    public String getBlankElementPath() {
        return blankElementPath;
    }

    public void setBlankElementPath(String blankElementPath) {
        this.blankElementPath = blankElementPath;
    }

    public String getBlankElementContent() {
        return blankElementContent;
    }

    public void setBlankElementContent(String blankElementContent) {
        this.blankElementContent = blankElementContent;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getLogicPageNum() {
        return logicPageNum;
    }

    public void setLogicPageNum(int logicPageNum) {
        this.logicPageNum = logicPageNum;
    }

    public Set<MarkInfo> getMarkKeys() {
        return markKeys;
    }

    public void setMarkKeys(Set<MarkInfo> markKeys) {
        this.markKeys = markKeys;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getElementAvaliableDataPath() {
        return elementAvaliableDataPath;
    }

    public void setElementAvaliableDataPath(String elementAvaliableDataPath) {
        this.elementAvaliableDataPath = elementAvaliableDataPath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMarginPath() {
        return marginPath;
    }

    public void setMarginPath(String marginPath) {
        this.marginPath = marginPath;
    }

    public String getFooterPath() {
        return footerPath;
    }

    public void setFooterPath(String footerPath) {
        this.footerPath = footerPath;
    }

    public String getMarginContent() {
        return marginContent;
    }

    public void setMarginContent(String marginContent) {
        this.marginContent = marginContent;
    }

    public String getFooterContent() {
        return footerContent;
    }

    public void setFooterContent(String footerContent) {
        this.footerContent = footerContent;
    }

    public boolean isJsMode() {
        return jsMode;
    }

    public void setJsMode(boolean jsMode) {
        this.jsMode = jsMode;
    }
}
