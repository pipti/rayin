package com.rayin.htmladapter.base.model.tplconfig;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 模板配置
 *
 * @date 2019-08-25
 * @author Jonah wz
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
