package ink.rayin.app.web.vo;

import ink.rayin.app.web.model.OrganizationModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-02-07 09:30
 **/
@Data
public class UserVo implements Serializable{
    private static final long serialVersionUID = 1L;
    String id;
    String username;
    String password;
    String realName;
    int roleId;
    List<OrganizationModel> orgs;
}
