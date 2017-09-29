package com.sftc.web.controller.app;

import com.sftc.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("qiniu")
public class QiniuController extends BaseController {

    @RequestMapping(value = "/uptoken", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> uptoken() throws Exception {
        return qiniuService.returnUptoken();
    }
}
