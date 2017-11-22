package com.sftc.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sftc.tools.api.*;
import com.sftc.tools.md5.MD5Util;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.others.WXAPPUser;
import com.sftc.web.model.vo.swaggerRequest.APPUserParamVO;
import com.sftc.web.model.vo.swaggerRequest.UserParamVO;
import com.sftc.web.model.others.WXUser;
import com.sftc.web.service.MessageService;
import com.sftc.web.service.UserService;
import net.sf.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
import static com.sftc.tools.constant.ThirdPartyConstant.WX_APP_UNIONID;
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

    private static final String WXC_USER = "WXC";
    private static final String APP_USER = "APP";

    public APIResponse login(APIRequest request) throws Exception {
        UserParamVO userParamVO = (UserParamVO) request.getRequestParam();
        APIStatus status = SUCCESS;
        String auth_url = WX_AUTHORIZATION + userParamVO.getJs_code();
        WXUser wxUser = APIResolve.getWxUserWithUrl(auth_url);
        User user = null;
        Map<String, String> tokenInfo = new HashMap<String, String>();
        if (wxUser.getOpenid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wxUser.getOpenid());
            if (userList.size() > 1) {
                return APIUtil.paramErrorResponse("出现重复用户，请填写用户反馈");
            }
            if (userList.size() == 1) {
                user = userList.get(0);
            }
            if (user == null) {
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
            } else {
                user.setOpen_id(wxUser.getOpenid());
                user.setSession_key(wxUser.getSession_key());
                user.setCreate_time(Long.toString(System.currentTimeMillis()));
                //更新头像和昵称
                if (userParamVO.getName() != null && userParamVO.getAvatar() != null) {
                    logger.info("更新头像: " + userParamVO.getAvatar());
                    logger.info("更新名字: " + userParamVO.getName());
                    user.setAvatar(userParamVO.getAvatar());
                    user.setName(userParamVO.getName());
                    userMapper.updateUserOfAvatar(user);
                }
                Token token = tokenMapper.getTokenById(user.getId());
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
                    tokenMapper.updateToken(token);
                }
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
        Map<String, String> tokenInfo = new HashMap<String, String>();
        if (wxUser.getOpenid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wxUser.getOpenid());
            if (userList.size() > 1) {
                return APIUtil.paramErrorResponse("出现重复用户，请填写用户反馈");
            }
            if (userList.size() == 1) {
                user = userList.get(0);
            }
            if (user == null) {
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
            } else {
//                user.setOpen_id(wxUser.getOpenid());// 不更新
                user.setSession_key(wxUser.getSession_key());
//                user.setCreate_time(Long.toString(System.currentTimeMillis()));//不更新
                //更新头像和昵称
                if (userParamVO.getName() != null && userParamVO.getAvatar() != null) {
                    logger.info("更新头像: " + userParamVO.getAvatar());
                    logger.info("更新名字: " + userParamVO.getName());
                    user.setAvatar(userParamVO.getAvatar());
                    user.setName(userParamVO.getName());
                }
                userMapper.updateUserOfAvatar(user);
                Token token = tokenMapper.getTokenById(user.getId());
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
                tokenInfo.put("token", token.getLocal_token());
                tokenInfo.put("user_id", (token.getUser_id() + ""));
                Token paramtoken = tokenMapper.getTokenById(token.getUser_id());
                if (paramtoken.getAccess_token() != null && !"".equals(paramtoken.getAccess_token())) { // 获取顺丰 access_token和uuid的内容
                    HashMap<String, String> map = checkAccessToken(token.getUser_id(), paramtoken);
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

    /**
     * APP登录获取unionid
     */
    public APIResponse appLogin(APIRequest request) throws Exception {

        APPUserParamVO appUserParamVO = (APPUserParamVO) request.getRequestParam();
        String auth_url = WX_APP_UNIONID + appUserParamVO.getCode();
        WXAPPUser wxappUser = APIResolve.getWxAPPUserWithUrl(auth_url);
        User user = null;
        Map<String, String> tokenInfo = new HashMap<String, String>();
        if (wxappUser.getOpenid() != null && wxappUser.getUnionid() != null) {
            List<User> userList = userMapper.selectUserByOpenid(wxappUser.getOpenid());
            if (userList.size() > 1) {
                return APIUtil.paramErrorResponse("出现重复用户，请填写用户反馈");
            }
            if (userList.size() == 1) {
                user = userList.get(0);
            }
            if (user == null) {
                User user2 = new User();
                user2.setOpen_id(wxappUser.getOpenid());
                user2.setUnionid(wxappUser.getUnionid());
                user2.setChanel(APP_USER);
                user2.setCreate_time(Long.toString(System.currentTimeMillis()));
                //加入头像和昵称
                if (appUserParamVO.getName() != null && appUserParamVO.getAvatar() != null) {
                    user2.setAvatar(appUserParamVO.getAvatar());
                    user2.setName(appUserParamVO.getName());
                    userMapper.appInsertWithAvatarAndName(user2);
                } else {
                    userMapper.insertUnionId(user2);
                }
                //构建新的token
                String myToken = makeToken(user2.getCreate_time(), user2.getOpen_id());
                Token token = new Token(user2.getId(), myToken);
                tokenMapper.addToken(token);
                tokenInfo.put("token", myToken);
                tokenInfo.put("user_id", (user2.getId() + ""));
            } else {
                //更新头像和昵称
                if (appUserParamVO.getName() != null && appUserParamVO.getAvatar() != null) {
                    logger.info("更新头像: " + appUserParamVO.getAvatar());
                    logger.info("更新名字: " + appUserParamVO.getName());
                    user.setAvatar(appUserParamVO.getAvatar());
                    user.setName(appUserParamVO.getName());
                }
                if (user.getUnionid() == null || user.getUnionid().equals("")) {
                    String unionid = wxappUser.getUnionid();
                    userMapper.updateUnionId(unionid, user.getId());
                }
                userMapper.updateUserOfAvatar(user);
                Token token = tokenMapper.getTokenById(user.getId());
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
                tokenInfo.put("token", token.getLocal_token());
                tokenInfo.put("user_id", (token.getUser_id() + ""));
                Token paramtoken = tokenMapper.getTokenById(token.getUser_id());
                if (paramtoken.getAccess_token() != null && !"".equals(paramtoken.getAccess_token())) { // 获取顺丰 access_token和uuid的内容
                    HashMap<String, String> map = checkAccessToken(token.getUser_id(), paramtoken);
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
            return APIUtil.submitErrorResponse(wxappUser.getErrmsg(), wxappUser);
        }
        return APIUtil.getResponse(SUCCESS, tokenInfo);
    }

    public Token getToken(int id) {
        return tokenMapper.getToken(id);
    }

    private String makeToken(String str1, String str2) {
        String s = MD5Util.MD5(str1 + str2);
        return s.substring(0, s.length() - 10);
    }

    // 验证access_token是否有效 通过访问merchant/me接口 但只针对有access_token的用户
    private HashMap<String, String> checkAccessToken(int user_id, Token paramtoken) throws Exception {
        Token token = tokenMapper.getTokenById(user_id);
        //验证 access_token 如果error则用refresh_token
        String old_accesstoken = token.getAccess_token();
        JSONObject resJSONObject = catchSFLogin(old_accesstoken);
        HashMap<String, String> map = new HashMap<String, String>();
        if (resJSONObject.containsKey("error")) { // 旧的token失效 要刷新
            // 访问sf刷新token的链接
            StringBuilder postUrl = new StringBuilder(SF_GET_TOKEN);
            postUrl.append("?refresh_token=");
            postUrl.append(token.getRefresh_token());
            HttpPost post = new HttpPost(postUrl.toString());
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


            map.put("access_token", newAccess_token);
        } else { // 旧的token有效
            map.put("uuid", resJSONObject.getJSONObject("merchant").getString("uuid"));
            map.put("access_token", old_accesstoken);
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

    //更新个人信息
    public APIResponse updatePersonMessage(APIRequest apiRequest) throws Exception {
        Object requestParam = apiRequest.getRequestParam();
        JSONObject jsonObject = JSONObject.fromObject(requestParam);
        JSONObject merchants = jsonObject.getJSONObject("merchant");
//      String json = merchants.toString();
        String new_name = merchants.getString("name");
        String email = merchants.getString("email");
        String uuid = merchants.getJSONObject("address").getString("uuid");
        String province = merchants.getJSONObject("address").getString("province");
        String city = merchants.getJSONObject("address").getString("city");
        String region = merchants.getJSONObject("address").getString("region");
        String street = merchants.getJSONObject("address").getString("street");
        String zipcode = merchants.getJSONObject("address").getString("zipcode");
        String receiver = merchants.getJSONObject("address").getString("receiver");
        String mobile = merchants.getJSONObject("address").getString("mobile");
        String longitude = merchants.getJSONObject("address").getString("longitude");
        String latitude = merchants.getJSONObject("address").getString("latitude");
        String json = "{\"merchant\":{\"name\":\"" + new_name + "\",\"attributes\":{},\"summary\":{},\"" +
                "email\":\"" + email + "\",\"address\":{\"type\":\"LIVE\",\"country\":\"中国\",\"province\":\"" + province + "\",\"" +
                "city\":\"" + city + "\",\"region\":\"" + region + "\",\"street\":\"" + street + "\",\"zipcode\":\"" + zipcode + "\",\"receiver\":" +
                "\"" + receiver + "\",\"mobile\":\"" + mobile + "\",\"marks\":{},\"longitude\":\"" + longitude + "\",\"latitude\":\"" + latitude + "\",\"uuid\":\"" + uuid + "\"}}}";
        String access_token = null;
        if (jsonObject.getString("token") != null && !(jsonObject.getString("token")).equals("")) {
            access_token = jsonObject.getString("token");
        } else {
            access_token = SFTokenHelper.COMMON_ACCESSTOKEN;
        }
        RequestBody rb = RequestBody.create(null, json);
        Request request = new Request.Builder().
                url(SF_LOGIN).
                addHeader("Content-Type", "application/json").
                addHeader("PushEnvelope-Device-Token", access_token)
                .put(rb).build();
        OkHttpClient client = new OkHttpClient();
        okhttp3.Response response = client.newCall(request).execute();
        if (response.code() == 200) return APIUtil.getResponse(SUCCESS, response.message());//正常情况返回null
        return APIUtil.logicErrorResponse("更新个人信息失败", response.body());
    }

    /**
     * 下面是CMS的内容
     */
    public APIResponse selectUserListByPage(APIRequest request) throws Exception {

        HttpServletRequest httpServletRequest = request.getRequest();
        // 此处封装了 User的构造方法
        User user = new User(httpServletRequest);
        int pageNumKey = Integer.parseInt(httpServletRequest.getParameter("pageNumKey"));
        int pageSizeKey = Integer.parseInt(httpServletRequest.getParameter("pageSizeKey"));
        //  使用lambab表达式 配合pageHelper实现对用户列表和查询相关信息的统一查询
        PageInfo<Object> pageInfo = PageHelper.startPage(pageNumKey, pageSizeKey).doSelectPageInfo(() -> userMapper.selectByPage(user));
        //  处理结果
        if (pageInfo.getList().size() == 0) {
            return APIUtil.selectErrorResponse("搜索到的结果数为0，请检查查询条件", null);
        } else {
            return APIUtil.getResponse(SUCCESS, pageInfo);
        }
    }
}
