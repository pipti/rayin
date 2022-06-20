package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-02-06 21:32
 **/
@Data
@Accessors(chain = true)
public class OrganizationModel {
    private String organizationId;
    private int orgNumber;
    private String organizationName;
}
