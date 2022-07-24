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
package ink.rayin.htmladapter.openhtmltopdf.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取关键字在pdf中的位置
 *
 * @author Janah Wang / 王柱
 * 2022-02-08
 */
@Slf4j
public class PdfBoxPositionFindByKey {

    /**
     * 搜索PDF关键字
     * @param document
     * @param page
     * @param keyword
     * @return List<TextPositionSequence>
     * @throws IOException
     */
    private static List<TextPositionSequence> findKeywordPage(PDDocument document, int page, String keyword) throws IOException {
        final List<TextPositionSequence> hits = new ArrayList<>();
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                TextPositionSequence word = new TextPositionSequence(textPositions);
                String string = word.toString();

                int fromIndex = 0;
                int index;
                while ((index = string.indexOf(keyword, fromIndex)) > -1) {
                    hits.add(word.subSequence(index, index + keyword.length()));
                    fromIndex = index + 1;
                }
                super.writeString(text, textPositions);
            }
        };

        stripper.setSortByPosition(true);
        stripper.setStartPage(page);
        stripper.setEndPage(page);
        stripper.getText(document);
        return hits;
    }

    /**
     * 搜索PDF关键字
     * @param pdfData pdf byte
     * @param keyword 关键字
     * @return 关键字列表
     *  float[0]:pageNum, float[1]:x, float[2]:y
     * @throws IOException IOException
     */
    public static List<float[]> findKeywordPagesPostions(byte[] pdfData, String keyword) throws IOException {
        PDDocument document = PDDocument.load(pdfData);
        List<float[]> pagesPositions = new ArrayList<>();
        for (int page = 1; page <= document.getNumberOfPages(); page++) {
            List<float[]> pagePositions = findKeywordPagePostions(pdfData,keyword,page);
            pagesPositions.addAll(pagePositions);
        }

        return pagesPositions;
    }

    /**
     * 指定页搜索关键字
     * @param pdfData pdf byte
     * @param keyword 关键字
     * @param pageNum 页码
     * @return
     *  float[0]:pageNum, float[1]:x, float[2]:y
     * @throws IOException IOException
     */
    public static List<float[]> findKeywordPagePostions(byte[] pdfData, String keyword, int pageNum) throws IOException {
        PDDocument document = PDDocument.load(pdfData);
        List<float[]> positions = new ArrayList<>();
        List<TextPositionSequence> hits = findKeywordPage(document, pageNum, keyword);
        for (TextPositionSequence hit : hits) {
            float[] pos = new float[]{pageNum,hit.getX(),hit.getY()};
            positions.add(pos);
        }
        return positions;
    }

}
