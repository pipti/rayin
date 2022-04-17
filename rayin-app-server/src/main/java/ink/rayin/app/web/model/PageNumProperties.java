package ink.rayin.app.web.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 单模板页码配置
 *
 * @date 2019-08-25
 * @author Eric Wang
 */
@Data
@Accessors(chain = true)
public class PageNumProperties {

    private boolean pageNumIsFirstPage;
    private boolean pageNumIsCalculate;
    private boolean pageNumIsDisplay;
    private List<PageNumDisplayPos> pageNumDisplayPoss;

}
