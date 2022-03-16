package ink.rayin.htmladapter.base;

import java.io.IOException;
import java.util.List;

/**
 * 关键字位置信息
 * @author Jonah wz
 */
public interface PositionFindByKey {
    public List<float[]> findKeywordPagesPostions(byte[] pdfData, String keyword) throws IOException;
    public List<float[]> findKeywordPagePostions(byte[] pdfData, String keyword,int pageNum) throws IOException;
}
