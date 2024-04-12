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
 * 单模板页码配置
 *
 * @author Janah Wang / 王柱 2019-08-25
 */

public class PageNumProperties {

    private boolean pageNumIsFirstPage;
    private boolean pageNumIsCalculate;
    private boolean pageNumIsDisplay;
    private List<PageNumDisplayPos> pageNumDisplayPoss;

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
}
