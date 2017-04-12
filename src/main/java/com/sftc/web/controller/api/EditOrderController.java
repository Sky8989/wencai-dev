package com.sftc.web.controller.api;

import com.sftc.tools.api.APIResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.controller.api
 * @Description:
 * @date 2017/4/12
 * @Time 下午3:42
 */
@Controller
@RequestMapping("edit")
public class EditOrderController {

    @RequestMapping(value = "/order", method = {RequestMethod.GET, RequestMethod.POST}, headers = "api-version=1")
    public @ResponseBody
    APIResponse editOrder() {
        
    }
}
