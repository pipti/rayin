package ink.rayin.app.web.model;

import lombok.Data;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: 作者名字
 * @create: 2020-06-24 15:37
 **/
@Data
public class UserIndexesUser extends UserIndexes{
    private String indexName;
    private String indexNameChn;
    private String indexValue;
}
