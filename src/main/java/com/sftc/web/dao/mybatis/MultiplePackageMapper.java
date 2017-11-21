package com.sftc.web.dao.mybatis;

import com.sftc.web.model.dto.MultiplePackageDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 好友多包裹
 *
 * @author ： CatalpaFlat
 * @date ：Create in 14:14 2017/11/17
 */
public interface MultiplePackageMapper {
    MultiplePackageDTO querySourceOrderInfoByOrderID(String orderId);

    List<MultiplePackageDTO> queryTargetsOrderInfoByOrderID(String orderId);

    void updateQuotesUUidById(@Param(value = "orderExpressId") String orderExpressId,
                              @Param(value = "quotesUuid") String quotesUuid);

    void updateOrderExpressById(Map<String, Object> map);

    void updateorderById(@Param(value = "orderId")String orderId,@Param(value = "groupUUId") String groupUUId);

    String queryUserOpenIDByGroupUUId(String groupUUId);
}
