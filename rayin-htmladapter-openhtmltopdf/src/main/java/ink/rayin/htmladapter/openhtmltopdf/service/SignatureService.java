package ink.rayin.htmladapter.openhtmltopdf.service;

import ink.rayin.htmladapter.base.model.tplconfig.KeywordSignatureProperty;
import ink.rayin.htmladapter.base.model.tplconfig.MultiplySignatureProperty;
import ink.rayin.htmladapter.openhtmltopdf.utils.PDFBoxPositionFindByKey;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 获取关键字服务
 * @author Jonsh wz
 */
public class SignatureService {

    /**
     * 根据关键字进行签章
     * @param bos
     * @param keywordSignatureProperty
     */
    public static List<float[]> keywordSignature(ByteArrayOutputStream bos,
                                                              KeywordSignatureProperty keywordSignatureProperty) throws IOException {
        return PDFBoxPositionFindByKey.findKeywordPagesPostions(bos.toByteArray(),
                keywordSignatureProperty.getKeyword());
    }

    /**
     * 根据关键字进行搜索
     * @param bos
     * @param keys
     */
    public static List<float[]> findKeywords(ByteArrayOutputStream bos,
                                             String keys) throws IOException {
        return PDFBoxPositionFindByKey.findKeywordPagesPostions(bos.toByteArray(),
                keys);
    }

    /**
     * 多页签名
     */
    public void multiplySignature(MultiplySignatureProperty multiplySignatureProperty){

    }
}
