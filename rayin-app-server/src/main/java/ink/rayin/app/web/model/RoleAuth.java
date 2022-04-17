package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: 作者名字
 * @create: 2020-11-12 18:11
 **/
@Data
@Accessors(chain = true)
public class RoleAuth {
    private String url;
    private int roleId;
}
