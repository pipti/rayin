package ink.rayin.app.web.controller;

import ink.rayin.app.web.model.RestResponse;
import ink.rayin.app.web.annotation.OrgId;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author eric
 */
public class FileBatchGenerateController {

    @PostMapping(value = {"/fileBatchGen/pdf/query"})
    public RestResponse organizationPDFQuery(@OrgId String orgId){
        return null;
    }
}
