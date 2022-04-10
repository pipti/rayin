package ink.rayin.app.web.service;

import ink.rayin.app.web.model.FolderFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @program: rayin-app-parent
 * @description:
 * @author: tangyongmao
 * @create: 2020-07-07 21:38
 **/
public interface IOrganizationResourceService {
    String createFolder(String orgId, String userId, FolderFile folderFile);
    List<FolderFile> getFolderAndFiles(String orgId, String userId, String currentFolderId);
    String uploadFile(String orgId, String userId, String currentFolderId,MultipartFile multipartFile);
    List<FolderFile> getAllPath(String orgId, String currentFolderId);
    InputStream getFileStream(String objectId);
    String getUrl(String objectId);
    String getUrl(String orgId, String[] folderFiles);
    String deleteFolderFiles(String orgId,List<FolderFile> folderFiles);
    String shareFolderFiles(String currentOrgId, String toOrgId, String userId, List<FolderFile> folderFiles);
}
