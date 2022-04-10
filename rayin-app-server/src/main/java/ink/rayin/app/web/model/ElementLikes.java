package ink.rayin.app.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-11-11 11:08
 **/
@Data
public class ElementLikes implements Serializable{
    private String organizationId;
    private String elementId;
    private String userId;
}
