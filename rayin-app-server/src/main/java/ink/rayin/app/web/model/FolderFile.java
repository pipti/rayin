package ink.rayin.app.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-07-07 22:10
 **/
@Data
public class FolderFile implements Serializable{
    private String objectId;
    private String folderId;
    private String dataName;
    private String dataType;
    private String fatherFolderId;
    private String date;
    private String fileType;
    private String folderLevel;
    private String url;
}
