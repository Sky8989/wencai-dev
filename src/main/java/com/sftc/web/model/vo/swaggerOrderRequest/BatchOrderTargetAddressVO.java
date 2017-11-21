package com.sftc.web.model.vo.swaggerOrderRequest;

import com.sftc.web.model.vo.swaggerRequest.TargetCoordinateVO;
import lombok.Getter;
import lombok.Setter;

/**
 * 收件人地址+经纬度
 *
 * @author ： CatalpaFlat
 * @date ：Create in 13:45 2017/11/20
 */
public class BatchOrderTargetAddressVO {
    @Getter
    @Setter
    private BatchTargetAddressVO address;
    @Getter
    @Setter
    private TargetCoordinateVO coordinate;
}
