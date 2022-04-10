package ink.rayin.app.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-06-24 15:12
 **/
@Data
public class UserIndexes implements Serializable{
    private String indexId;
    private String organizationId;
    private String userId;
    private String value;
}
