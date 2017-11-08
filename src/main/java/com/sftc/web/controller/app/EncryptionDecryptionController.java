package com.sftc.web.controller.app;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.service.EncryptionDecryptionService;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import static com.sftc.tools.common.ControllerHelper.responseEntityBuilder;

@Controller
@Api(description = "加密")
@RequestMapping("decryption")
public class EncryptionDecryptionController extends BaseController {
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
