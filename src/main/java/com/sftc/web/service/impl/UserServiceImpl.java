package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.tools.common.EmojiFilter;
import com.sftc.tools.md5.Md5Util;
import com.sftc.tools.token.TokenUtils;
import com.sftc.tools.utils.UserAttributesUtil;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.others.WXUser;
import com.sftc.web.model.vo.swaggerRequest.UserMerchantCheckVO;
import com.sftc.web.model.vo.swaggerRequest.UserMerchantVO;
import com.sftc.web.model.vo.swaggerRequest.UserMerchantsRequestVO;
import com.sftc.web.model.vo.swaggerRequest.UserParamVO;
import com.sftc.web.service.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.CustomConstant.*;
import static com.sftc.tools.constant.CustomConstant.ACCESS_TOKEN;
import static com.sftc.tools.constant.DkConstant.DK_USER_AVATAR_DEFAULT;
import static com.sftc.tools.constant.SfConstant.*;
import static com.sftc.tools.constant.SfConstant.SF_LOGIN;
import static com.sftc.tools.constant.ThirdPartyConstant.WX_AUTHORIZATION;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenMapper tokenMapper;


    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());


    @Override
    public ApiResponse login(UserParamVO userParamVO) throws Exception {
        String authUrl = WX_AUTHORIZATION + userParamVO.getJs_code();
        WXUser wxUser = ApiResolve.getWxUserWithUrl(authUrl);
        User user = null;
        Map<String, String> tokenInfo = new HashMap<>(3);
        if (wxUser.getOpenid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wxUser.getOpenid());
            if (userList.size() > 1) {
                return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "出现重复用户，请填写用户反馈");
            }
            if (userList.size() == 1) {
                user = userList.get(0);
            }
            if (user == null) {
                loginNewUser(wxUser, userParamVO, tokenInfo);
            } else {
                user.setAttributes(UserAttributesUtil.getUserAttributesByOpenId(wxUser.getOpenid()));
                user.setOpenId(UserAttributesUtil.getUserAttributesByOpenId(wxUser.getOpenid()));

                Token token = tokenMapper.getTokenByUUId(user.getUuid());
                updateUserToken(userParamVO, token, user);

                tokenInfo.put(HEARD_TOKEN, token.getLocal_token());
                tokenInfo.put("user_uuid", (token.getUser_uuid() + ""));
            }
        } else {
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), wxUser.getErrmsg());
        }
        return ApiUtil.getResponse(SUCCESS, tokenInfo);
    }

    /**
     * 合并后的登陆接口 前台来一个jscode
     */
    @Override
    public ApiResponse superLogin(UserParamVO userParamVO) throws Exception {
        String authUrl = WX_AUTHORIZATION + userParamVO.getJs_code();
        WXUser wxUser = ApiResolve.getWxUserWithUrl(authUrl);
        User user = null;
        Map<String, String> tokenInfo = new HashMap<>(10);
        if (wxUser.getOpenid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wxUser.getOpenid());
            if (userList.size() > 1) {
                return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "出现重复用户，请填写用户反馈");
            }
            if (userList.size() == 1) {
                user = userList.get(0);
            }
            if (user == null) {
                loginNewUser(wxUser, userParamVO, tokenInfo);
            } else {
                user.setOpenId(UserAttributesUtil.getUserAttributesByOpenId(wxUser.getOpenid()));
                Token token = tokenMapper.getTokenByUUId(user.getUuid());
                updateUserToken(userParamVO, token, user);

                tokenInfo.put(HEARD_TOKEN, token.getLocal_token());
                tokenInfo.put("user_uuid", (token.getUser_uuid() + ""));
                Token paramToken = tokenMapper.getTokenByUUId(token.getUser_uuid());
                // 获取顺丰 access_token和uuid的内容
                if (paramToken.getAccess_token() != null && !"".equals(paramToken.getAccess_token())) {
                    HashMap<String, String> map = checkAccessToken(token.getUser_uuid());
                    if (map.containsKey(ERROR)) {
                        return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "获取access_token和uuid的内容错误", JSONObject.fromObject(map.get(ERROR)));
                    }
                    String uuidKey = "uuid";
                    if (map.containsKey(uuidKey)) {
                        tokenInfo.put(uuidKey, map.get(uuidKey));
                    } else {
                        tokenInfo.put(uuidKey, user.getUuid());
                    }

                    tokenInfo.put(ACCESS_TOKEN, map.get(ACCESS_TOKEN));
                }
                //若有手机号 则加入手机号
                if (user.getMobile() != null && !"".equals(user.getMobile())) {
                    tokenInfo.put("mobile", user.getMobile());
                }
            }
        } else {
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), wxUser.getErrmsg());
        }
        return ApiUtil.getResponse(SUCCESS, tokenInfo);
    }

    /**
     * 检查绑定状态
     */
    @Override
    public ApiResponse checkBindStatus() throws Exception {

        JSONObject responseObject = new JSONObject();
        String userUUID = TokenUtils.getInstance().getUserUUID();
        String accessToken = TokenUtils.getInstance().getAccessToken();
        boolean isBind = false;
        // 有绑定手机号的用户，access_token不为空
        if (StringUtils.isNotBlank(accessToken)) {
            // 验证access_token是否有效
            HashMap loginInfo = checkAccessToken(userUUID);
            // access_token有效，或者使用refresh_token刷新了access_token
            if ((!loginInfo.containsKey(ERROR)) && loginInfo.containsKey(ACCESS_TOKEN)) {
                isBind = true;
            }
        }
        responseObject.put("is_bind", isBind);

        return ApiUtil.getResponse(SUCCESS, responseObject);
    }

    @Override
    public ApiResponse obtainUserWallets(int type) {
        /*---------------------------------------------------------------- 请求参数获取 --------------------------------------------------------------------------------*/
        String userUUId = TokenUtils.getInstance().getUserUUID();
        if (StringUtils.isBlank(userUUId)) {
            return ApiUtil.error(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value(), "The user_uuid does not exist");
        }
        String typeStr = null;
        if (type == 0) {
            typeStr = WALLET_TYPE_CASH;
        } else if (type == 1) {
            typeStr = WALLET_TYPE_AWARD;
        }
        /*---------------------------------------------------------------- sf请求 --------------------------------------------------------------------------------*/
        StringBuilder url = new StringBuilder();
        if (type < 2) {
            url.append(SF_WALLET)
                    .append(userUUId)
                    .append("?type=")
                    .append(typeStr);
        } else {
            url.append(SF_WALLET)
                    .append(userUUId);
        }
        HttpGet get = new HttpGet(url.toString());
        //获取公共access_token
        String accessToken = TokenUtils.getInstance().getAccessToken();
        get.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = ApiGetUtil.get(get);

        /*---------------------------------------------------------------- sf请求响应 --------------------------------------------------------------------------------*/
        if (StringUtils.isBlank(res)) {
            logger.error("获取用户钱包失败，sf返回体res为空");
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "failure to obtain User Wallets");
        }
        JSONObject resultObject = JSONObject.fromObject(res);
        if (resultObject.containsKey(ERROR)) {
            logger.error("获取用户钱包失败，响应体：" + resultObject);
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "failure to wallet", resultObject.get("error"));
        }
        String walletStr;

        JSONArray walletArray = null;
        JSONObject walletJson = null;
        if (type < 2) {
            walletStr = "wallet";
            walletJson = resultObject.getJSONObject(walletStr);
        } else {
            walletStr = "wallets";
            walletArray = resultObject.getJSONArray(walletStr);
        }

        if (!resultObject.containsKey(walletStr)) {
            logger.error("获取用户钱包失败，获取不到wallet字段，响应体：" + resultObject);
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "Can not find wallet");
        }

        /*---------------------------------------------------------------- 响应封装 --------------------------------------------------------------------------------*/
        String balanceStr = "balance";
        JSONObject walletResult = new JSONObject();
        JSONObject balanceResult = new JSONObject();
        Object balance;

        if (walletArray != null) {
            walletArray = resultObject.getJSONArray(walletStr);
            for (Object object : walletArray) {
                JSONObject jsonObject = (JSONObject) object;
                if (walletJsonContainBalance(resultObject, balanceStr, jsonObject)) {
                    return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "Can not find balance");
                }
                String jsonType = jsonObject.getString("type");
                balanceResult.put("balance", jsonObject.get(balanceStr));

                if (StringUtils.equals(jsonType, WALLET_TYPE_CASH)) {
                    walletResult.put("cash", balanceResult);
                } else if (StringUtils.equals(jsonType, WALLET_TYPE_AWARD)) {
                    walletResult.put("award", balanceResult);
                }
            }
        } else {
            if (walletJsonContainBalance(resultObject, balanceStr, walletJson)) {
                return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "Can not find balance");
            }
            assert walletJson != null;
            balance = walletJson.get(balanceStr);
            balanceResult.put("balance", balance);
            if (type == 0) {
                walletResult.put("cash", balanceResult);
            } else if (type == 1) {
                walletResult.put("award", balanceResult);
            }
        }

        JSONObject responseJson = new JSONObject();

        responseJson.put("wallet", walletResult);

        return ApiUtil.getResponse(ApiStatus.SUCCESS, responseJson);
    }

    /**
     * 获取余额明细
     */
    @Override
    public ApiResponse obtainBalanceDetailed(int limit, int offset) {
        if (limit < 0 || offset < 0) {
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "Request parameter error!");
        }
         /*---------------------------------------------------------------- 请求参数获取 --------------------------------------------------------------------------------*/
        String userUUId = TokenUtils.getInstance().getUserUUID();
        if (StringUtils.isBlank(userUUId)) {
            return ApiUtil.error(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.value(), "The user_uuid does not exist");
        }
        /*---------------------------------------------------------------- sf请求 --------------------------------------------------------------------------------*/
        String url = SF_BALANCER_DETAILED +
                userUUId +
                "?status=" +
                BALANCER_DETAILED_STATUS +
                "&type=" +
                BALANCER_DETAILED_TYPE +
                "&limit=" +
                limit +
                "&offset=" +
                offset + "&channel_type=CREDITS";

        HttpGet get = new HttpGet(url);
        //获取公共access_token
        String accessToken = TokenUtils.getInstance().getAccessToken();
        get.addHeader("PushEnvelope-Device-Token", accessToken);
        String res = ApiGetUtil.get(get);

        /*---------------------------------------------------------------- sf请求响应 --------------------------------------------------------------------------------*/
        if (StringUtils.isBlank(res)) {
            logger.error("获取余额明细失败，sf返回体res为空");
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "failure to obtain User BalanceDetailed");
        }
        JSONObject sfResultObject = JSONObject.fromObject(res);

        if (sfResultObject.containsKey(ERROR)) {
            logger.error("获取余额明细失败，响应体：" + sfResultObject);
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "failure to BalanceDetailed", sfResultObject.get("error"));
        }
        String transactionsStr = "transactions";
        if (!sfResultObject.containsKey(transactionsStr)) {
            logger.error("获取余额明细失败，响应体：" + sfResultObject);
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), "failure to BalanceDetailed", sfResultObject.get("error"));
        }
        JSONArray transactionsArray = sfResultObject.getJSONArray(transactionsStr);

        for (Object object : transactionsArray) {
            JSONObject jsonObject = (JSONObject) object;
            jsonObject.remove("user");
            jsonObject.remove("request");
            jsonObject.remove("events");
            String type = jsonObject.getString("type");
            switch (type) {
                case BALANCER_DETAILED_TYPE_INCOME:
                    jsonObject.put("title", "延时送达");
                    break;
                case BALANCER_DETAILED_TYPE_EXPEND:
                    jsonObject.put("title", "下单抵扣");
                    break;
                case BALANCER_DETAILED_TYPE_REFUND:
                    jsonObject.put("title", "退款");
                    break;
                default:
                    break;
            }
        }

        JSONObject responseJson = new JSONObject();
        responseJson.put("total_pages", sfResultObject.get("total_pages"));
        responseJson.put("transactions", transactionsArray);

        return ApiUtil.getResponse(ApiStatus.SUCCESS, responseJson);
    }

    private boolean walletJsonContainBalance(JSONObject resultObject, String balanceStr, JSONObject jsonObject) {
        if (!jsonObject.containsKey(balanceStr)) {
            logger.error("获取用户钱包失败，获取不到balance字段，响应体：" + resultObject);
            return true;
        }
        return false;
    }

    @Override
    public Token getToken(int id) {
        return tokenMapper.getToken(id);
    }

    private String makeToken(String str1, String str2) {
        String s = Md5Util.md5(str1 + str2);
        return s != null ? s.substring(0, s.length() - 10) : null;
    }

    /**
     * 新用户 头像 名称 token 等存储
     */
    private void loginNewUser(WXUser wxUser, UserParamVO userParamVO, Map<String, String> tokenInfo) {
        User user2 = new User();
        user2.setAttributes(UserAttributesUtil.getUserAttributesByOpenId(wxUser.getOpenid()));
        user2.setOpenId(UserAttributesUtil.getUserAttributesByOpenId(wxUser.getOpenid()));
        //加入头像和昵称
        if (userParamVO.getName() != null && userParamVO.getAvatar() != null) {
            String avatar = StringUtils.isBlank(userParamVO.getAvatar()) ? DK_USER_AVATAR_DEFAULT : userParamVO.getAvatar();
            user2.setAvatar(avatar);
            user2.setName(userParamVO.getName());
            userMapper.insertWithAvatarAndName(user2);
        } else {
            userMapper.insertOpenid(user2);
        }
        //构建新的token
        String myToken = makeToken(user2.getCreate_at(), user2.getOpenId());
        Token token = new Token(user2.getUuid(), myToken);
        tokenMapper.addToken(token);
        tokenInfo.put(HEARD_TOKEN, myToken);
        tokenInfo.put("user_uuid", (user2.getUuid() + ""));
    }

    /**
     * 老用户更新 token 和 有效时间
     */
    private void updateUserToken(UserParamVO userParamVO, Token token, User user) {
        if (userParamVO.getName() != null && userParamVO.getAvatar() != null) {
            String avatar = StringUtils.isBlank(userParamVO.getAvatar()) ? DK_USER_AVATAR_DEFAULT : userParamVO.getAvatar();
            user.setAvatar(avatar);
            user.setName(userParamVO.getName());
            userMapper.updateUserOfAvatar(user);
        }
        if (token == null) {
            token = new Token(user.getUuid(), makeToken(user.getCreate_at(), user.getOpenId()));
            tokenMapper.addToken(token);

        } else {
            String gmtModified = Long.toString(System.currentTimeMillis());
            if (Long.parseLong(gmtModified) > Long.parseLong(token.getGmt_expiry())) {
                token.setGmt_expiry(Long.toString(System.currentTimeMillis() + 2592000000L));
                String myToken = makeToken(gmtModified, user.getOpenId());
                token.setLocal_token(myToken);
            }
            token.setGmt_modified(gmtModified);
            // 此处更新 localtoken
            tokenMapper.updateToken(token);
        }
    }

    /**
     * 验证access_token是否有效 通过访问merchant/me接口 但只针对有access_token的用户
     */
    private HashMap<String, String> checkAccessToken(String userUUId) throws Exception {

        Token token = tokenMapper.getTokenByUUId(userUUId);
        // 验证access_token 如果error则用refresh_token去刷新
        User user = userMapper.selectUserByUserUUId(userUUId);
        UserMerchantVO userMerchant = new UserMerchantVO();
        UserMerchantCheckVO userMerchantCheck = new UserMerchantCheckVO();
        userMerchant.setName("");
        if (StringUtils.isNotBlank(user.getMobile())) {
            userMerchant.setMobile(user.getMobile());
        }
        userMerchantCheck.setMerchant(userMerchant);
        String postStr = new Gson().toJson(userMerchantCheck);

        String oldAccesstoken = token.getAccess_token();
        JSONObject resJSONObject = catchSFLogin(oldAccesstoken);
        HashMap<String, String> map = new HashMap<>(3);
        // 旧的token失效 要刷新
        if (resJSONObject.containsKey(ERROR)) {
            // 访问sf刷新token的链接
            String postUrl = SF_GET_TOKEN.concat("?refresh_token=").concat(token.getRefresh_token());
            HttpPost post = new HttpPost(postUrl);
            String resPost = ApiPostUtil.post(postStr, post);
            JSONObject resPostJSONObject = JSONObject.fromObject(resPost);
            // 处理refresh刷新失败的情况
            if (resPostJSONObject.containsKey(ERROR)) {
                map.put(ERROR, resPost);
                return map;
            }
            // 更新 两个关键token
            String newAccessToken = resPostJSONObject.getJSONObject(HEARD_TOKEN).getString(ACCESS_TOKEN);
            String newRefreshToken = resPostJSONObject.getJSONObject(HEARD_TOKEN).getString(REFRESH_TOKEN);
            token.setAccess_token(newAccessToken);
            token.setRefresh_token(newRefreshToken);
            tokenMapper.updateToken(token);
            // 返回新的access_token
            map.put(ACCESS_TOKEN, newAccessToken);
        } else { // 旧的access_token有效
            map.put(ACCESS_TOKEN, oldAccesstoken);
        }
        return map;
    }

    /**
     * 通过access_token访问sf接口 获取uuid的公告方法
     */
    private JSONObject catchSFLogin(String oldAccesstoken) throws Exception {
        HttpGet httpGet = new HttpGet(SF_LOGIN);
        httpGet.addHeader("PushEnvelope-Device-Token", oldAccesstoken);
        String res = ApiGetUtil.get(httpGet);
        return JSONObject.fromObject(res);
    }


    /**
     * 更新个人信息 下单时调用
     */
    @Override
    public ApiResponse updatePersonMessage(UserMerchantsRequestVO requestParam) throws Exception {
        // 参数处理
        requestParam.getMerchant().getAddress().setType("LIVE");
        requestParam.getMerchant().getAddress().setUuid(TokenUtils.getInstance().getUserUUID());
        requestParam.getMerchant().getAddress().setZipcode("");
        requestParam.getMerchant().getAddress().setReceiver("");
        requestParam.getMerchant().setName(EmojiFilter.replaceEmoji(requestParam.getMerchant().getName()));
        requestParam.getMerchant().getAddress().setReceiver(EmojiFilter.replaceEmoji(requestParam.getMerchant().getAddress().getReceiver()));

        String requestJson = new Gson().toJson(requestParam);
        String accessToken = TokenUtils.getInstance().getAccessToken();
        RequestBody rb = RequestBody.create(null, requestJson);
        Request request = new Request.Builder().
                url(SF_LOGIN).
                addHeader("Content-Type", "application/json").
                addHeader("PushEnvelope-Device-Token", accessToken)
                .put(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.code() != HttpStatus.SC_OK) {
            return ApiUtil.error(org.springframework.http.HttpStatus.BAD_REQUEST.value(), response.message());
        }
        return ApiUtil.getResponse(SUCCESS, null);
    }
}
