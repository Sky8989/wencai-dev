package com.sftc.web.service.impl;

import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.redis.UserUnpackingRedisDao;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.vo.swaggerRequest.UserUnpackingVO;
import com.sftc.web.service.UserUnpackingService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.sftc.tools.api.ApiStatus.SUCCESS;

@Service
public class UserUnpackingServiceImpl implements UserUnpackingService {

    private static final String DELIMITER = "_";

    @Resource
    private TokenMapper tokenMapper;
    @Resource
    private UserUnpackingRedisDao userUnpackingRedis;

    @Override
    public ApiResponse unpacking(UserUnpackingVO userUnpackingVO, HttpServletRequest request) {

        String token = request.getHeader(CustomConstant.HEARD_TOKEN);
        if (StringUtils.isBlank(token)) {
            return ApiUtil.error(HttpStatus.UNAUTHORIZED.value(), "token is null");
        }
        User user = tokenMapper.tokenInterceptor(token);
        if (user == null) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "user does not exist");
        }
        if (userUnpackingVO == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "the request body is empty");
        }
        String userUUId = user.getUuid();
        String orderId = userUnpackingVO.getOrder_id();
        int type = userUnpackingVO.getType();
        String value = this.mosaicKey(orderId, userUUId);

        JSONArray packArray = userUnpackingRedis.getUserUnpacking();

        JSONObject responseJson = new JSONObject();
        if (CollectionUtils.isNotEmpty(packArray)) {
            if (!packArray.contains(value)) {
                if (type == 0) {
                    responseJson.put("is_pack", false);
                } else {
                    //进行拆包
                    packArray.add(value);
                    userUnpackingRedis.setUserUnpacking(packArray);
                    responseJson.put("pack", true);
                }
            } else {
                responseJson.put("is_pack", true);
            }
        } else {
            userUnpackingRedis.setDefaultUserUnpackingValue();
            responseJson.put("is_pack", false);
        }

        return ApiUtil.getResponse(SUCCESS, responseJson);
    }

    /**
     * 拼接redis键值对
     */
    private String mosaicKey(String orderId, String userUUId) {
        StringBuilder sb = new StringBuilder();
        sb.append(orderId);
        sb.append(DELIMITER);
        sb.append(userUUId);
        return sb.toString();
    }
}
