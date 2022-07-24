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
package ink.rayin.htmladapter.base;

import java.io.IOException;
import java.util.List;

/**
 * 关键字位置信息
 * @author Janah Wang / 王柱
 */
public interface PositionFindByKey {
    public List<float[]> findKeywordPagesPostions(byte[] pdfData, String keyword) throws IOException;
    public List<float[]> findKeywordPagePostions(byte[] pdfData, String keyword,int pageNum) throws IOException;
}
