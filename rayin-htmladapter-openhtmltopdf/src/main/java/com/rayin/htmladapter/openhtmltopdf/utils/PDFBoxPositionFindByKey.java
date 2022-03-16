package com.rayin.htmladapter.openhtmltopdf.utils;

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
 * @author Jonah wz
 * @date 2022-02-08
 */
@Slf4j
public class PDFBoxPositionFindByKey {

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

//    public static void main(String[] args) throws IOException {
//        String filePath = "/Users/eric/Downloads/document.pdf";
//        File file = new File(filePath);
//        PDDocument document = PDDocument.load(file);
//        byte[] fb = IoUtil.toByteArray(new FileInputStream(file));
//        //PDFBoxPositionFindByKey pdfBoxPositionFindByKey = new PDFBoxPositionFindByKey();
//        log.debug(JSON.toJSON(PDFBoxPositionFindByKey.findKeywordPagesPostions(fb,"PMSSIGN")).toString());
//    }

    /**
     * 搜索PDF关键字
     * @param pdfData
     * @param keyword
     * @return
     *  float[0]:pageNum, float[1]:x, float[2]:y
     * @throws IOException
     */
    public static List<float[]> findKeywordPagesPostions(byte[] pdfData, String keyword) throws IOException {
        PDDocument document = PDDocument.load(pdfData);
        //List<PageKeywordPositions> positions = new ArrayList<PageKeywordPositions>();
        List<float[]> pagesPositions = new ArrayList<>();
        for (int page = 1; page <= document.getNumberOfPages(); page++) {
            List<float[]> pagePositions = findKeywordPagePostions(pdfData,keyword,page);
            pagesPositions.addAll(pagePositions);
        }

        return pagesPositions;
    }

    /**
     * 指定页搜索关键字
     * @param pdfData
     * @param keyword
     * @param pageNum
     * @return
     *  float[0]:pageNum, float[1]:x, float[2]:y
     * @throws IOException
     */
    public static List<float[]> findKeywordPagePostions(byte[] pdfData, String keyword, int pageNum) throws IOException {
        PDDocument document = PDDocument.load(pdfData);
        List<float[]> positions = new ArrayList<>();
        List<TextPositionSequence> hits = findKeywordPage(document, pageNum, keyword);
        for (TextPositionSequence hit : hits) {
            TextPosition lastPosition = hit.textPositionAt(hit.length() - 1);
            float[] pos = new float[]{pageNum,hit.getX(),hit.getY()};
            positions.add(pos);
        }
        return positions;
    }
}
