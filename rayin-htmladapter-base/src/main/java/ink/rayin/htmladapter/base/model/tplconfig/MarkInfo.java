package ink.rayin.htmladapter.base.model.tplconfig;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 标记信息
 * @Date 2020-03-22
 * @author Jonah wz
 */
@Data
public class MarkInfo {

    private String keyword;
    private int pageNum;
    private float x;
    private float y;
    private float width;
    private float height;
}
