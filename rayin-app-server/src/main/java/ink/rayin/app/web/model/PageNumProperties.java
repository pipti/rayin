package ink.rayin.app.web.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 单模板页码配置
 *
 * @date 2019-08-25
 * @author Eric Wang
 */
public class PageNumProperties {

    @Getter
    @Setter
    private boolean pageNumIsFirstPage;

    @Getter
    @Setter
    private boolean pageNumIsCalculate;

    @Getter
    @Setter
    private boolean pageNumIsDisplay;

    @Getter
    @Setter
    private List<PageNumDisplayPos> pageNumDisplayPoss;

}