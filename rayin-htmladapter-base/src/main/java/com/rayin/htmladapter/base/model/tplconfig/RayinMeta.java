package com.rayin.htmladapter.base.model.tplconfig;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 自定义源数据
 *
 * @date 2019-08-25
 * @author Jonah wz
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
