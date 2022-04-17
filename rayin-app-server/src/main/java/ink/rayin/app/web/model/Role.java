package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-11-12 18:19
 **/
@Data
@Accessors(chain = true)
public class Role {
    private int roleId;
    private String roleName;
}
