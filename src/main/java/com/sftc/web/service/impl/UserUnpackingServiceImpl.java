package com.sftc.web.service.impl;

import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.web.model.dao.mybatis.TokenMapper;
import com.sftc.web.model.dao.redis.UserUnpackingRedisDao;
import com.sftc.web.model.vo.swaggerRequestVO.UserUnpackingVO;
import com.sftc.web.model.User;
import com.sftc.web.service.UserUnpackingService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.*;

@Service
public class UserUnpackingServiceImpl implements UserUnpackingService {

    @Resource
    private UserUnpackingRedisDao userUnpackingRedis;
    @Resource
    private TokenMapper tokenMapper;

    private static final String  DELIMITER = "_";
    private static final String HEARD_TOKEN = "token";

    @Override
    public APIResponse unpacking(APIRequest apiRequest) {
        UserUnpackingVO userUnpackingVO = (UserUnpackingVO) apiRequest.getRequestParam();

        Map header = apiRequest.getHeader();
        String token = null;
        if (header.size()!=0)
             token = (String) header.get(HEARD_TOKEN);

        User user = tokenMapper.tokenInterceptor(token);
        if (user==null){
            return APIUtil.paramErrorResponse(SELECT_FAIL.getMessage());
        }
        if(userUnpackingVO==null){
            return APIUtil.paramErrorResponse(PARAM_ERROR.getMessage());
        }
        int user_id = user.getId();
        String order_id = userUnpackingVO.getOrder_id();
        int type = userUnpackingVO.getType();
        String key = this.mosaicKey(order_id, user_id);
        String userUnpacking = userUnpackingRedis.getUserUnpacking(key);
        JSONObject json = new JSONObject();

        if (userUnpacking==null){
            if (type==0)
                json.put("is_pack",false);
            else {
                userUnpackingRedis.setUserUnpacking(key,"true");
                json.put("pack",true);
            }
        }else
            json.put("is_pack",true);
        return APIUtil.getResponse(SUCCESS, json);
    }
    /**拼接redis键值对*/
    private String mosaicKey(String order_id, int user_id){
        StringBuilder sb = new StringBuilder();
        sb.append(order_id);
        sb.append(DELIMITER);
        sb.append(user_id);
        return sb.toString();
    }
}
