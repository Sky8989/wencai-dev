package com.sftc.web.controller.api;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.controller.AbstractBasicController;
import com.sftc.web.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("token")
public class TokenController extends AbstractBasicController {
    @Resource
    private TokenService tokenService;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public @ResponseBody
    APIResponse login(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        return tokenService.token(apiRequest);
    }

    @RequestMapping(value = "/token2", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<APIResponse> login2(@RequestBody Object object) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(object);
        APIResponse apiResponse = APIUtil.paramErrorResponse("测试错误");
        ResponseEntity<APIResponse> responseEntity;
        if (apiResponse.getState() == 200) {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.OK);
            return responseEntity;
        } else if (apiResponse.getState() == 500) {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            return responseEntity;
        } else if (apiResponse.getState() == 400) {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
            return responseEntity;
        } else {
            responseEntity = new ResponseEntity<APIResponse>(apiResponse, HttpStatus.NOT_ACCEPTABLE);
            return responseEntity;
        }
    }

}
