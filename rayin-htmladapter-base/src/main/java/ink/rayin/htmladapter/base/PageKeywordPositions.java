package ink.rayin.htmladapter.base;

import java.util.List;

/**
 * 关键字位置bean
 * @author Jonah wz
 */
public class PageKeywordPositions {
    private String content;

    private List<float[]> positions;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    /**
     * float[0]:页码
     * float[1]:x 坐标
     * float[2]:y 坐标
     */
    public List<float[]> getPositions() {
        return positions;
    }

    public void setPostions(List<float[]> positions) {
        this.positions = positions;
    }
}
