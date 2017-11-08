package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.service.EncryptionDecryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

import static com.sftc.tools.common.ControllerHelper.responseEntityBuilder;

@Controller
@RequestMapping("decryption")
public class EncryptionDecryptionController extends AbstractBasicController {
    @Resource
    private EncryptionDecryptionService encryptionDecryptionService;


    @RequestMapping(value = "/encryptedData", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<APIResponse> uptoken(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return responseEntityBuilder(encryptionDecryptionService.encryptedData(apiRequest));
    }
}
