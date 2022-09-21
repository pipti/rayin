package ink.rayin.htmladapter.base.model.tplconfig;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


/**
 * 目录实体
 * 2022-08-21
 * @author wangzhu / 王柱
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Catalog {
    private String title;
    private long pageNum;
    private JSONArray catalogs;
}
