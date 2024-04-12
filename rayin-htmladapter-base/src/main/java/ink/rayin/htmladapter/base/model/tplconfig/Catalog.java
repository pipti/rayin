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


/**
 * 目录实体
 * @author Janah Wang / 王柱 2022-08-21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Catalog {
    private String title;
    private long pageNum;
    private List catalogs;
    private MarkInfo markInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public List getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List catalogs) {
        this.catalogs = catalogs;
    }

    public MarkInfo getMarkInfo() {
        return markInfo;
    }

    public void setMarkInfo(MarkInfo markInfo) {
        this.markInfo = markInfo;
    }
}
