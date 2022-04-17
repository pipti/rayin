package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tym
 * @create: 2020-11-10 16:48
 **/
@Data
@Accessors(chain = true)
public class ElementShare implements Serializable {
    private String elementId;
    private String organizationId;
    private String note;
    private String userId;
    private String title;
    private int likes;
    private int stores;
    private int views;

    private String headImg;

    @TableField(exist = false)
    private String searchKey;
    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    String content;
    @TableField(exist = false)
    String testData;
    @TableField(exist = false)
    String userName;
    @TableField(exist = false)
    String nickName;
    @TableField(exist = false)
    String moblie;
    @TableField(exist = false)
    String elementThum;
}
