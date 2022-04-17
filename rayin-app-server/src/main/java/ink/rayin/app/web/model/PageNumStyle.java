package ink.rayin.app.web.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 单模板页码样式
 *
 * @date 2019-08-25
 * @author Eric Wang
 */
@Data
@Accessors(chain = true)
public class PageNumStyle {
    private int fontSize;
    private String textDescript;
    private String fontStyle;

}
