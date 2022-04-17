package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * [users Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-04-01 WangZhu created<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Data
@Accessors(chain = true)
public class Users {
    private String id;
    private String username;
    private String password;
    private String roleId;
    private String organizationId;
    private String realName;
}
