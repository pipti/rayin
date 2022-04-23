package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-06-22 18:40
 **/
@Data
@Accessors(chain = true)
public class OrganizationData implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    private String dataId;
    private String organizationId;
    private String dataName;
    private String data;
    private String userId;
    private String createDate;
    private String updateDate;
}
