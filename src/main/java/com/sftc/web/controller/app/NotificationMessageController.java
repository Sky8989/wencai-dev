package com.sftc.web.controller.app;


import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.BaseController;
import com.sftc.web.model.vo.swaggerRequestVO.UpdateIsReadVO;
import com.sftc.web.model.vo.swaggerResponse.NotificaionMessageListVO;
import com.sftc.web.model.vo.swaggerResponse.ResponseMessageVO;
import com.sftc.web.service.NotificationMessageService;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(description = "通知")
@RequestMapping("notification")
public class NotificationMessageController extends BaseController {
    @Resource
    private NotificationMessageService notificationMessageService;

    @ApiOperation(value = "未读通知列表",httpMethod = "GET",notes = "一是作为寄件人，好友填了包裹地址时收到的，包含订单信息；\n" +
            "二是作为收件人，收到他人寄的包裹时收到的。",response = NotificaionMessageListVO.class)
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 401,message = "The query fails"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    APIResponse placeOrder(HttpServletRequest request) throws Exception {
        return notificationMessageService.getMessage(new APIRequest(request));
    }

    @ApiOperation(value = "更新通知消息读取状态",httpMethod = "PUT",response = ResponseMessageVO.class)
    @ApiResponses({
            @ApiResponse(code = 400,message = "Parameters of the abnormal"),
            @ApiResponse(code = 401,message = "The query fails"),
            @ApiResponse(code = 402,message = "The submit fails"),
            @ApiResponse(code = 500,message = "System exceptions")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody()
    APIResponse updateIsRead(@RequestBody UpdateIsReadVO updateIsReadVO) throws Exception {
        APIRequest apiRequest = new APIRequest();
        apiRequest.setRequestParam(updateIsReadVO);
        return notificationMessageService.updateMessage(apiRequest);
    }
}
