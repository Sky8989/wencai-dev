package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.mapper.MessageMapper;
import com.sftc.web.mapper.OrderMapper;
import com.sftc.web.model.Message;
import com.sftc.web.model.Order;
import com.sftc.web.service.NotificationMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;

@Service
public class NotificationMessageServiceImpl implements NotificationMessageService {
    @Resource
    private MessageMapper messageMapper;

    @Resource
    private OrderMapper orderMapper;

    public APIResponse getMessage(APIRequest request) {

        // Param
        String userID = (String) request.getParameter("user_id");
        if (userID == null || userID.equals(""))
            return APIUtil.paramErrorResponse("user_id不能为空");

        int user_id = Integer.parseInt(userID);
        if (user_id < 1)
            return APIUtil.paramErrorResponse("user_id无效");

        // Result
        List<Message> messageList = messageMapper.selectUnReadMessageList(user_id);
        for (Message message : messageList) {
            int express_id = message.getExpress_id();
            Order order = orderMapper.selectOrderDetailByExpressId(express_id);
            if (message.getMessage_type().equals("RECEIVE_ADDRESS")) {
                // 只有"收到地址"的消息才需要订单数据
                message.setOrder(order);
            }
        }

        return APIUtil.getResponse(SUCCESS, messageList);
    }

    public APIResponse updateMessage(int id) {
        messageMapper.updateIsRead(id);
        return APIUtil.getResponse(SUCCESS, null);
    }
}
