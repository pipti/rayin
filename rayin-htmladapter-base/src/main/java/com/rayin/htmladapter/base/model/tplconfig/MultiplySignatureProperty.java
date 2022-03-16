package com.rayin.htmladapter.base.model.tplconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * 签名签章属性
 *
 * @date 2019-09-08
 * @author Jonah wz
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
