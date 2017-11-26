package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.tools.sf.SFOrderHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.others.WXUser;
import com.sftc.web.model.vo.swaggerRequest.UserMerchantsRequestVO;
import com.sftc.web.model.vo.swaggerRequest.UserParamVO;
import com.sftc.web.service.MessageService;
import com.sftc.web.service.UserService;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.SF_GET_TOKEN;
import static com.sftc.tools.constant.SFConstant.SF_LOGIN;
import static com.sftc.tools.constant.ThirdPartyConstant.WX_AUTHORIZATION;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenMapper tokenMapper;

    @Resource
    private MessageService messageService;

    private static final String postStr = "{\"merchant\":{" +
            "\"mobile\":\"13797393543\"" + "}," +
            "\"message\":{" + "\"content\":\"4444\"}}";

    public APIResponse login(APIRequest request) throws Exception {
        UserParamVO userParamVO = (UserParamVO) request.getRequestParam();
        APIStatus status = SUCCESS;
        String auth_url = WX_AUTHORIZATION + userParamVO.getJs_code();
        WXUser wxUser = APIResolve.getWxUserWithUrl(auth_url);
        User user = null;
        Map<String, String> tokenInfo = new HashMap<>();
        if (wxUser.getOpenid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wxUser.getOpenid());
            if (userList.size() > 1) {
                return APIUtil.paramErrorResponse("出现重复用户，请填写用户反馈");
            }
            if (userList.size() == 1) {
                user = userList.get(0);
            }
            if (user == null) {
                loginNewUser(wxUser, userParamVO, tokenInfo);
            } else {
                user.setOpen_id(wxUser.getOpenid());
                user.setSession_key(wxUser.getSession_key());
                user.setCreate_time(Long.toString(System.currentTimeMillis()));

                Token token = tokenMapper.getTokenById(user.getId());
                updateUserToken(userParamVO, token, user);

                tokenInfo.put("token", token.getLocal_token());
                tokenInfo.put("user_id", (token.getUser_id() + ""));
            }
        } else {
            return APIUtil.submitErrorResponse(wxUser.getErrmsg(), wxUser);
        }
        return APIUtil.getResponse(status, tokenInfo);
    }

    //  合并后的登陆接口 前台来一个jscode
    public APIResponse superLogin(APIRequest request) throws Exception {

        UserParamVO userParamVO = (UserParamVO) request.getRequestParam();
        String auth_url = WX_AUTHORIZATION + userParamVO.getJs_code();
        WXUser wxUser = APIResolve.getWxUserWithUrl(auth_url);
        User user = null;
        Map<String, String> tokenInfo = new HashMap<>();
        if (wxUser.getOpenid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wxUser.getOpenid());
            if (userList.size() > 1) {
                return APIUtil.paramErrorResponse("出现重复用户，请填写用户反馈");
            }
            if (userList.size() == 1) {
                user = userList.get(0);
            }
            if (user == null) {
                loginNewUser(wxUser, userParamVO, tokenInfo);
            } else {
                user.setSession_key(wxUser.getSession_key());

                Token token = tokenMapper.getTokenById(user.getId());
                updateUserToken(userParamVO, token, user);

                tokenInfo.put("token", token.getLocal_token());
                tokenInfo.put("user_id", (token.getUser_id() + ""));
                Token paramtoken = tokenMapper.getTokenById(token.getUser_id());
                if (paramtoken.getAccess_token() != null && !"".equals(paramtoken.getAccess_token())) { // 获取顺丰 access_token和uuid的内容
                    HashMap<String, String> map = checkAccessToken(token.getUser_id());
                    if (map.containsKey("error")) {
                        return APIUtil.submitErrorResponse("refresh_token failed", JSONObject.fromObject(map.get("error")));
                    }
                    if (map.containsKey("uuid")) {
                        tokenInfo.put("uuid", map.get("uuid"));
                    } else tokenInfo.put("uuid", user.getUuid());

                    tokenInfo.put("access_token", map.get("access_token"));
                }
                //若有手机号 则加入手机号
                if (user.getMobile() != null && !"".equals(user.getMobile())) {
                    tokenInfo.put("mobile", user.getMobile());
                }
            }
        } else {
            return APIUtil.submitErrorResponse(wxUser.getErrmsg(), wxUser);
        }
        return APIUtil.getResponse(SUCCESS, tokenInfo);
    }

    // 检查绑定状态
    public APIResponse checkBindStatus() throws Exception {

        JSONObject responseObject = new JSONObject();
        int userId = TokenUtils.getInstance().getUserId();
        String access_token = TokenUtils.getInstance().getAccess_token();
        boolean isBind = false;
        if (StringUtils.isNotBlank(access_token)) { // 有绑定手机号的用户，access_token不为空
            // 验证access_token是否有效
            HashMap loginInfo = checkAccessToken(userId);
            // access_token有效，或者使用refresh_token刷新了access_token
            if ((!loginInfo.containsKey("error")) && loginInfo.containsKey("access_token")) {
                isBind = true;
            }
        }
        responseObject.put("is_bind", isBind);

        return APIUtil.getResponse(SUCCESS, responseObject);
    }

    public Token getToken(int id) {
        return tokenMapper.getToken(id);
    }

    private String makeToken(String str1, String str2) {
        String s = MD5Util.MD5(str1 + str2);
        return s != null ? s.substring(0, s.length() - 10) : null;
    }

    //新用户 头像 名称 token 等存储
    private void loginNewUser(WXUser wxUser, UserParamVO userParamVO, Map<String, String> tokenInfo) {
        User user2 = new User();
        user2.setOpen_id(wxUser.getOpenid());
        user2.setSession_key(wxUser.getSession_key());
        user2.setCreate_time(Long.toString(System.currentTimeMillis()));
        //加入头像和昵称
        if (userParamVO.getName() != null && userParamVO.getAvatar() != null) {
            user2.setAvatar(userParamVO.getAvatar());
            user2.setName(userParamVO.getName());
            userMapper.insertWithAvatarAndName(user2);
        } else {
            userMapper.insertOpenid(user2);
        }
        //构建新的token
        String myToken = makeToken(user2.getCreate_time(), user2.getOpen_id());
        Token token = new Token(user2.getId(), myToken);
        tokenMapper.addToken(token);
        tokenInfo.put("token", myToken);
        tokenInfo.put("user_id", (user2.getId() + ""));
    }

    //老用户更新 token 和 有效时间
    private void updateUserToken(UserParamVO userParamVO, Token token, User user) {
        if (userParamVO.getName() != null && userParamVO.getAvatar() != null) {
            logger.info("更新头像: " + userParamVO.getAvatar());
            logger.info("更新名字: " + userParamVO.getName());
            user.setAvatar(userParamVO.getAvatar());
            user.setName(userParamVO.getName());
            userMapper.updateUserOfAvatar(user);
        }
        if (token == null) {
            token = new Token(user.getId(), makeToken(user.getCreate_time(), user.getOpen_id()));
            tokenMapper.addToken(token);
        } else {
            String gmt_modified = Long.toString(System.currentTimeMillis());
            if (Long.parseLong(gmt_modified) > Long.parseLong(token.getGmt_expiry())) {
                token.setGmt_expiry(Long.toString(System.currentTimeMillis() + 2592000000L));
                String myToken = makeToken(gmt_modified, user.getOpen_id());
                token.setLocal_token(myToken);
            }
            token.setGmt_modified(gmt_modified);
            // 此处更新 localtoken
            tokenMapper.updateToken(token);
        }
    }

    // 验证access_token是否有效 通过访问merchant/me接口 但只针对有access_token的用户
    private HashMap<String, String> checkAccessToken(int user_id) throws Exception {

        Token token = tokenMapper.getTokenById(user_id);
        // 验证access_token 如果error则用refresh_token去刷新
        String oldAccesstoken = token.getAccess_token();
        JSONObject resJSONObject = catchSFLogin(oldAccesstoken);
        HashMap<String, String> map = new HashMap<>();
        if (resJSONObject.containsKey("error")) { // 旧的token失效 要刷新
            // 访问sf刷新token的链接
            String postUrl = SF_GET_TOKEN.concat("?refresh_token=").concat(token.getRefresh_token());
            HttpPost post = new HttpPost(postUrl);
            String resPost = APIPostUtil.post(postStr, post);
            JSONObject resPostJSONObject = JSONObject.fromObject(resPost);
            // 处理refresh刷新失败的情况
            if (resPostJSONObject.containsKey("error")) {
                map.put("error", resPost);
                return map;
            }
            // 更新 两个关键token
            String newAccess_token = resPostJSONObject.getJSONObject("token").getString("access_token");
            String newRefresh_token = resPostJSONObject.getJSONObject("token").getString("refresh_token");
            token.setAccess_token(newAccess_token);
            token.setRefresh_token(newRefresh_token);
            tokenMapper.updateToken(token);
            // 返回新的access_token
            map.put("access_token", newAccess_token);
        } else { // 旧的access_token有效
            map.put("access_token", oldAccesstoken);
        }
        return map;
    }

    // 通过access_token访问sf接口 获取uuid的公告方法
    private JSONObject catchSFLogin(String old_accesstoken) throws Exception {
        HttpGet httpGet = new HttpGet(SF_LOGIN);
        httpGet.addHeader("PushEnvelope-Device-Token", old_accesstoken);
        String res = APIGetUtil.get(httpGet);
        return JSONObject.fromObject(res);
    }

//    // 解除绑定操作，原微信号，解除原有手机号
//    public APIResponse deleteMobile(APIRequest request) throws Exception {
//        Integer user_id = TokenUtils.getInstance().getUserId();
//        User user = userMapper.selectUserByUserId(user_id);
//        Token tokenById = tokenMapper.getTokenById(user_id);
//        if (user != null) {// 验空
//            if (user.getMobile() != null && !"".equals(user.getMobile())) {
//                //清除 手机号 uuid access_token 和 refresh_token
//                user.setMobile("");
//                user.setUuid("");
//                userMapper.updateUser(user);
//                tokenById.setAccess_token("");
//                tokenById.setRefresh_token("");
//                tokenMapper.updateToken(tokenById);
//                return APIUtil.getResponse(SUCCESS, user_id + "用户解除手机绑定成功");
//            } else {// 无手机号
//                return APIUtil.submitErrorResponse("该用户未绑定手机号，请勿进行操作", null);
//            }
//        } else {
//            return APIUtil.submitErrorResponse("无该用户，请检查参数", null);
//        }
//    }
//
//    // 修改手机号码 即重新绑定新手机号
//    public APIResponse updateMobile(APIRequest apiRequest) throws Exception {
//        Object requestParam = apiRequest.getRequestParam();
//        // 1 验证手机号可用性
//        JSONObject jsonObject = JSONObject.fromObject(requestParam);
//        String mobile = jsonObject.getJSONObject("merchant").getString("mobile");
//        User user = userMapper.selectUserByPhone(mobile);
//        if (user != null) {
//            return APIUtil.submitErrorResponse("手机号已被人使用过，请检查手机号", mobile);
//        }
//        // 2 走注册流程
//        return messageService.register(apiRequest);
//    }

    // 修改手机号码 即重新绑定新手机号
//    public APIResponse updateMobile(APIRequest apiRequest) throws Exception {
//        Object requestParam = apiRequest.getRequestParam();
//        // 1 验证手机号可用性
//        JSONObject jsonObject = JSONObject.fromObject(requestParam);
//        String mobile = jsonObject.getJSONObject("merchant").getString("mobile");
//        int user_id = jsonObject.getInt("user_id");
//        User user = userMapper.selectUserByPhone(mobile);
//        if (user != null) {
//            return APIUtil.submitErrorResponse("手机号已被人使用过，请检查手机号", mobile);
//        }
//        // 2 走注册流程
//        return messageService.register(apiRequest);
//    }

    //更新个人信息 下单时调用
    public APIResponse updatePersonMessage(APIRequest apiRequest) throws Exception {
        // 参数处理
        UserMerchantsRequestVO requestParam = (UserMerchantsRequestVO) apiRequest.getRequestParam();
        requestParam.getMerchant().getAddress().setType("LIVE");
        requestParam.getMerchant().getAddress().setUuid(TokenUtils.getInstance().getUserUUID());
        requestParam.getMerchant().getAddress().setZipcode("");
        requestParam.getMerchant().getAddress().setReceiver("");

        String requestJson = new Gson().toJson(requestParam);
        String access_token = TokenUtils.getInstance().getAccess_token();
        RequestBody rb = RequestBody.create(null, requestJson);
        Request request = new Request.Builder().
                url(SF_LOGIN).
                addHeader("Content-Type", "application/json").
                addHeader("PushEnvelope-Device-Token", access_token)
                .put(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            return APIUtil.submitErrorResponse(response.message(), null);
        }
        return APIUtil.getResponse(SUCCESS, null);
    }


}
