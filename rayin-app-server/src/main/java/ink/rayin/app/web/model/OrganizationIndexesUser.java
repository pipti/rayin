package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: 作者名字
 * @create: 2020-06-22 10:38
 **/
@Data
@Accessors(chain = true)
public class OrganizationIndexesUser extends OrganizationIndexes{
    private String username;
    private String indexValue;
}
