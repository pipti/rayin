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

import java.util.List;

/**
 * 模板配置
 *
 * @author Janah Wang / 王柱 2019-08-25
 */
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
    private boolean copyProtect = true;
    private List<PageNumDisplayPos> pageNumDisplayPoss;
    private List<SignatureProperty> signatureProperties;

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public WatermarkProperty getWatermark() {
        return watermark;
    }

    public void setWatermark(WatermarkProperty watermark) {
        this.watermark = watermark;
    }

    public boolean isCopyProtect() {
        return copyProtect;
    }

    public void setCopyProtect(boolean copyProtect) {
        this.copyProtect = copyProtect;
    }

    public List<PageNumDisplayPos> getPageNumDisplayPoss() {
        return pageNumDisplayPoss;
    }

    public void setPageNumDisplayPoss(List<PageNumDisplayPos> pageNumDisplayPoss) {
        this.pageNumDisplayPoss = pageNumDisplayPoss;
    }

    public List<SignatureProperty> getSignatureProperties() {
        return signatureProperties;
    }

    public void setSignatureProperties(List<SignatureProperty> signatureProperties) {
        this.signatureProperties = signatureProperties;
    }
}
