package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.MessageMapper;
import com.sftc.web.dao.mybatis.OrderMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.dto.MessageDTO;
import com.sftc.web.model.dto.OrderDTO;
import com.sftc.web.model.dto.OrderExpressDTO;
import com.sftc.web.model.entity.Message;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.vo.swaggerRequest.UpdateIsReadVO;
import com.sftc.web.service.NotificationMessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.DkConstant.DK_USER_AVATAR_DEFAULT;

@Service
public class NotificationMessageServiceImpl implements NotificationMessageService {
    @Resource
    private MessageMapper messageMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public ApiResponse getMessage() {

        // Param
        String userUUID = TokenUtils.getInstance().getUserUUID();
        if (StringUtils.isBlank(userUUID)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "user_uuid无效");
        }

        // Result
        List<Message> messageDTOList = messageMapper.selectUnReadMessageList(userUUID);
        List<MessageDTO> messageList = new ArrayList<>();
        for (Message message : messageDTOList) {
            int expressId = message.getExpress_id();
            if (expressId == 0) {
                continue;
            }

            OrderDTO orderDTO = orderMapper.selectOrderDetailByExpressId(expressId);
            List<OrderExpressDTO> orderExpresses = orderDTO.getOrderExpressList();
            for (OrderExpressDTO oe : orderExpresses) {
                User user = userMapper.selectUserByUserUUId(oe.getShip_user_uuid());
                if (user == null) {
                    oe.setShip_avatar(DK_USER_AVATAR_DEFAULT);
                } else {
                    oe.setShip_avatar(user.getAvatar());
                }
            }
            Gson gson = new Gson();
            if (StringUtils.equals(message.getMessage_type(), "RECEIVE_ADDRESS")) {
                // 只有"收到地址"的消息才需要订单数据
                MessageDTO messageDTO = gson.fromJson(gson.toJson(message), MessageDTO.class);
                messageDTO.setOrder(orderDTO);
                messageList.add(messageDTO);
            } else {
                MessageDTO messageDTO = gson.fromJson(gson.toJson(message), MessageDTO.class);
                messageList.add(messageDTO);
            }
        }

        return ApiUtil.getResponse(SUCCESS, messageList);
    }

    @Override
    public ApiResponse updateMessage(UpdateIsReadVO updateIsReadVO) {
        int id = updateIsReadVO.getMessage_id();
        messageMapper.updateIsRead(id);
        return ApiUtil.getResponse(SUCCESS, null);
    }
}
