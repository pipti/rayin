
package ink.rayin.app.web.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import ink.rayin.app.web.model.UserOrganization;
import ink.rayin.app.web.service.IPDFManagementService;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * [PDF文件管理服务].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-04-15 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
public class PDFManagementService implements IPDFManagementService {

    @Override
    public List minioOssQuery(UserOrganization uo, String searchKey) throws InvalidPortException, InvalidEndpointException, XmlParserException, InsufficientDataException, NoSuchAlgorithmException, IOException, InternalException, InvalidKeyException, InvalidBucketNameException, ErrorResponseException {
        MinioClient minioClient = new MinioClient(uo.getThirdStorageUrl(),
                uo.getThirdStorageAccessKey(),
                uo.getThirdStorageSecretKey());
        List rs = new ArrayList();
        if(StringUtils.isNotBlank(searchKey)){
            Iterable<Result<Item>> results = minioClient.listObjects(uo.getThirdStorageBucket(),searchKey);
            for (Result<Item> result : results) {
                Item item = result.get();
                rs.add(item);
            }

            return rs;
        }else{
            Iterable<Result<Item>> results = minioClient.listObjects(uo.getThirdStorageBucket());
            for (Result<Item> result : results) {
                Item item = result.get();
                rs.add(item);
            }
            return rs;
        }
    }

    @Override
    public List<OSSObjectSummary> aliyunOssQuery(UserOrganization uo, String searchKey){
        OSS client = new OSSClientBuilder().build(uo.getThirdStorageUrl(), uo.getThirdStorageAccessKey(), uo.getThirdStorageSecretKey());

        ObjectListing ol = client.listObjects(uo.getThirdStorageBucket());
        List<OSSObjectSummary> oll = ol.getObjectSummaries();
        client.shutdown();
        return oll;
    }
}
