package ink.rayin.htmladapter.base.model.tplconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * 单模板页码显示属性
 *
 * @date 2019-08-25
 * @author Jonah wz
 */
public class PageNumDisplayPos {
    @Getter
    @Setter
    private float x;
    @Getter
    @Setter
    private float y;
    @Getter
    @Setter
    private String mark;
    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private int fontSize;

    @Getter
    @Setter
    private String textDescript;

    @Getter
    @Setter
    private String fontFamily;
}
