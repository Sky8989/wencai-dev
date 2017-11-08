package com.sftc.web.controller.api;

import com.sftc.web.controller.AbstractBasicController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("qiniu")
public class QiniuController extends AbstractBasicController {

    @RequestMapping(value = "/uptoken", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> uptoken() throws Exception {
        return qiniuService.returnUptoken();
    }
}
