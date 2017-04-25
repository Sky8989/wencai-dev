package com.sftc.web.controller.api;

import com.sftc.web.controller.AbstractBasicController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description: 七牛文件上传控制器
 * @date 2017/4/19
 * @Time 下午3:40
 */
@Controller
@RequestMapping("qiniu")
public class QiniuController extends AbstractBasicController {

    @RequestMapping(value = "/uptoken", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, String> uptoken() throws Exception {
        return qiniuService.returnUptoken();
    }
}
