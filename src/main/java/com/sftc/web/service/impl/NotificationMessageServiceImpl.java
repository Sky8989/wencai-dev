package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.MessageMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.dto.MessageDTO;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.Message;
import com.sftc.web.service.NotificationMessageService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.DKConstant.DK_USER_AVATAR_DEFAULT;

@Service
public class NotificationMessageServiceImpl implements NotificationMessageService {
    @Resource
    private MessageMapper messageMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    private Gson gson = new Gson();

    public APIResponse getMessage(APIRequest request) {

        // Param
        Integer user_id = TokenUtils.getInstance().getUserId();
        if (user_id < 1)
            return APIUtil.paramErrorResponse("user_id无效");

        // Result
        List<Message> messageDTOList = messageMapper.selectUnReadMessageList(user_id);
        List<MessageDTO> messageList = new ArrayList<>();
        for (Message message : messageDTOList) {
            int express_id = message.getExpress_id();
            if (express_id == 0) continue;

            OrderDTO orderDTO = orderMapper.selectOrderDetailByExpressId(express_id);
            List<OrderExpressDTO> orderExpresses = orderDTO.getOrderExpressList();
            for (OrderExpressDTO oe : orderExpresses) {
                User user = userMapper.selectUserByUserId(oe.getShip_user_id());
                if (user == null) {
                    oe.setShip_avatar(DK_USER_AVATAR_DEFAULT);
                } else {
                    oe.setShip_avatar(user.getAvatar());
                }
            }

            if (message.getMessage_type().equals("RECEIVE_ADDRESS")) {
                // 只有"收到地址"的消息才需要订单数据
                MessageDTO messageDTO = gson.fromJson(gson.toJson(message), MessageDTO.class);
                messageDTO.setOrder(orderDTO);
                messageList.add(messageDTO);
            } else {
                MessageDTO messageDTO = gson.fromJson(gson.toJson(message), MessageDTO.class);
                messageList.add(messageDTO);
            }
        }

        return APIUtil.getResponse(SUCCESS, messageList);
    }

    public APIResponse updateMessage(APIRequest apiRequest) {
        JSONObject messageOBJ = JSONObject.fromObject(apiRequest.getRequestParam());
        int id = messageOBJ.getInt("message_id");
        messageMapper.updateIsRead(id);
        return APIUtil.getResponse(SUCCESS, null);
    }
}
