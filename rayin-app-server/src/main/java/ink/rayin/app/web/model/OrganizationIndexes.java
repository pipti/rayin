package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @program: rayin-app-parent
 * @description: OrganizationIndexes
 * @author: 作者名字
 * @create: 2020-06-21 20:40
 **/
@Data
@Accessors(chain = true)
public class OrganizationIndexes implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    private String indexId;
    private String organizationId;
    private String indexName;
    private String jsonPath;
    private String userId;
    private String createDate;
    private String updateDate;
    private String type;
    private String segment;
    private String indexNameChn;
}
