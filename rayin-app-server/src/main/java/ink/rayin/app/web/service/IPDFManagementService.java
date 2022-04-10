
package ink.rayin.app.web.service;


import ink.rayin.app.web.model.UserOrganization;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * [PDF文件管理服务接口].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-04-03 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
public interface IPDFManagementService {
    public List minioOssQuery(UserOrganization uo, String searchKey) throws InvalidPortException, InvalidEndpointException, XmlParserException, InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, InvalidKeyException, InvalidBucketNameException, ErrorResponseException;
    public List aliyunOssQuery(UserOrganization uo, String searchKey);
}
