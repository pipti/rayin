package ink.rayin.app.web.model;

import lombok.Data;

/**
 * 单模板页码显示属性
 *
 * @date 2019-08-25
 * @author Eric Wang
 */
@Data
public class PageNumDisplayPos {
    private int x;
    private int y;
    private String mark;
    private String content;
    private int fontSize = 13;
    private String textDescript;
    private String fontStyle;
}
