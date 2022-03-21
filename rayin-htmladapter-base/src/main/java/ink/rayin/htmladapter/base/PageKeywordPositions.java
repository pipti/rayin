/**
 * Copyright (c) 2022-2030, Janah wz 王柱 (carefreefly@163.com).
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
package ink.rayin.htmladapter.base;

import java.util.List;

/**
 * 关键字位置bean
 * @author Jonah wz
 */
public class PageKeywordPositions {
    private String content;

    private List<float[]> positions;

    /**
     * 获取内容
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
    /**
     *
     * 获取坐标信息
     *
     * @return
     * float[0]:页码
     * float[1]:x 坐标
     * float[2]:y 坐标
     */
    public List<float[]> getPositions() {
        return positions;
    }

    /**
     * 设置坐标
     * @param positions
     */
    public void setPostions(List<float[]> positions) {
        this.positions = positions;
    }
}
