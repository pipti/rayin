package ink.rayin.app.web.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: 作者名字
 * @create: 2020-06-24 15:37
 **/
@Data
@Accessors(chain = true)
public class UserIndexesUser extends UserIndexes{
    private String indexName;
    private String indexNameChn;
    private String indexValue;
}
