package com.sftc.web.model.dto;
import com.sftc.web.model.vo.displayMessage.PackageMessageVO;
import com.sftc.web.model.entity.Evaluate;
import com.sftc.web.model.entity.OrderExpress;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OrderExpressDTO extends OrderExpress {

    @Getter
    @Setter
    private int gift_card_id;    // 礼卡表id

    @Getter
    @Setter
    private String ship_avatar;  // extension 收件人头像

    @Getter
    @Setter
    private PackageMessageVO packageMessage;

    public OrderExpressDTO() {
    }

    @Getter
    @Setter
    private Evaluate evaluate;  // 评价信息

    @Getter
    @Setter
    private List<OrderExpress> orderExpressList;   //快递数组
}