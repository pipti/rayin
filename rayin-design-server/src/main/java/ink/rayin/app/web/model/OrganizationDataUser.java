package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-06-22 18:40
 **/
@Data
@Accessors(chain = true)
public class OrganizationDataUser extends OrganizationData{
    private String username;
}
