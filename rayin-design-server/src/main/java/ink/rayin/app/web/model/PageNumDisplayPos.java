package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 单模板页码显示属性
 *
 * @date 2019-08-25
 * @author Jonah Wang
 */
@Data
@Accessors(chain = true)
public class PageNumDisplayPos {
    private int x;
    private int y;
    private String mark;
    private String content;
    private int fontSize = 13;
    private String textDescript;
    private String fontStyle;
}
