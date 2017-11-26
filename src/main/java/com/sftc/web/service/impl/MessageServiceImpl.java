package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.UserInviteDao;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.entity.UserInvite;
import com.sftc.web.service.MessageService;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.sftc.tools.api.APIStatus.SUCCESS;
import static com.sftc.tools.constant.SFConstant.*;
import static com.sftc.tools.constant.WXConstant.*;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private TokenMapper tokenMapper;
    @Resource
    private UserInviteDao userInviteDao;

    @Resource
    private UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Gson gson = new Gson();

    /**
     * 获取短信验证码
     */
    public APIResponse getMessage(APIRequest apiRequest) {

        /*------------------------------------------------------------- 请求参数的获取 -------------------------------------------------------------------*/
        Object requestParam = apiRequest.getRequestParam();
        JSONObject jsonObject = JSONObject.fromObject(requestParam); // 调用顺丰注册接口的请求参数
        JSONObject jsonDevice = JSONObject.fromObject(requestParam); // 调用顺丰获取设备的请求参数
        String mobile = jsonObject.getJSONObject("message").getString("mobile");

        // 调用顺丰获取设备接口的参数整理
        jsonDevice.remove("message");
        jsonDevice.remove("invite");
        jsonDevice.getJSONObject("device").put("type", "WXC");

        /*------------------------------------------------------------- 设备ID的获取 -------------------------------------------------------------------*/
        // 调用顺丰设备接口
        HttpPost postDevice = new HttpPost(SF_DEVICE_URL);
        postDevice.addHeader("PushEnvelope-Device-Token", SFTokenHelper.COMMON_ACCESSTOKEN);
        String resDevice = APIPostUtil.post(gson.toJson(jsonDevice), postDevice);
        JSONObject resJSONDevice = JSONObject.fromObject(resDevice);
        // 处理获取顺丰设备ID接口的回调
        if (resJSONDevice.containsKey("error"))
            return APIUtil.submitErrorResponse("获取小程序设备ID错误", resJSONDevice);
        if (!resJSONDevice.containsKey("device"))
            return APIUtil.paramErrorResponse("Parameter device missing in sf-api callback.");
        if (!resJSONDevice.getJSONObject("device").containsKey("uuid"))
            return APIUtil.paramErrorResponse("Parameter device.uuid missing in sf-api callback.");
        // 正常情况，添加设备ID到获取验证码的请求headers中
        String deviceId = resJSONDevice.getJSONObject("device").getString("uuid");

        /*------------------------------------------------------------- 验证码的获取 -------------------------------------------------------------------*/
        // 顺丰验证码请求参数处理
        jsonObject.remove("device");
        jsonObject.remove("invite");
        jsonObject.getJSONObject("message").remove("mobile");
        jsonObject.getJSONObject("message").put("receiver", mobile);

        // 请求顺丰验证码接口
        HttpPost post = new HttpPost(SF_TAKE_MESSAGE_URL);
        post.addHeader("PushEnvelope-Device-ID", deviceId);
        String res = APIPostUtil.post(jsonObject.toString(), post);
        JSONObject resultObject = JSONObject.fromObject(res);
        // 处理验证码结果
        if (resultObject.containsKey("error"))
            return APIUtil.submitErrorResponse("请求验证码失败", resultObject.getJSONObject("error"));
        if (resultObject.containsKey("errors"))
            return APIUtil.submitErrorResponse("请求验证码失败", resultObject.getJSONArray("errors"));

        return APIUtil.getResponse(SUCCESS, resultObject);
    }

    /**
     * 验证短信验证码
     */
    public APIResponse messageCheck(APIRequest apiRequest) {

        JSONObject jsonObject = JSONObject.fromObject(apiRequest.getRequestParam());
        Integer user_id = TokenUtils.getInstance().getUserId();
        if (user_id != 0) {
            String mobile = jsonObject.getJSONObject("merchant").getString("mobile");

            // 验证手机号与user_id的匹配 根据手机号和 用户 id 查询用户
            User userByPhone = userMapper.selectUserByPhone(mobile);
            User userByUserId = userMapper.selectUserByUserId(user_id);
            Map<String, String> map = new HashMap<>(1, 1);
            if (userByUserId.getMobile() == null || "".equals(userByUserId.getMobile())) {
                // 用户的手机号为空 则判断该手机号是否被使用
                if (userByPhone != null) {
                    map.put("name", userByPhone.getName());
                    return APIUtil.submitErrorResponse("该手机号已被使用", map);
                }
            } else {// 用户已经绑定手机号 验证手机号不为用户手机号
                if (!mobile.equals(userByUserId.getMobile())) {
                    map.put("mobile", userByUserId.getMobile());
                    return APIUtil.submitErrorResponse("该用户已验证手机", map);
                }
            }

            /*------------------------------------------------------------- 调用顺丰获取 token 接口 验证手机验证码是否正确 -------------------------------------------------------------------*/
            JSONObject jsonInvite = jsonObject.getJSONObject("invite");  //用户注册时 相关邀请信息
            JSONObject attributeOBJ = jsonObject.getJSONObject("merchant").getJSONObject("attributes");

            String invite_code = null;
            if (jsonObject.getJSONObject("merchant").getJSONObject("attributes").containsKey("invite_code"))
                invite_code = jsonObject.getJSONObject("merchant").getJSONObject("attributes").getString("invite_code");
            //移除获取token时不需要的参数
            jsonObject.getJSONObject("merchant").remove("attributes");
            jsonObject.remove("invite");

            // 生成sf获取token的链接
            StringBuilder postUrl = new StringBuilder(SF_GET_TOKEN);

            // 调用顺丰获取token接口
            String str = gson.toJson(jsonObject);
            HttpPost post = new HttpPost(postUrl.toString());
            String res = APIPostUtil.post(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);

            /*------------------------------------------------------------- 验证成功调用登录接口 失败若为 NOT_FOUND 调用注册接口 -------------------------------------------------------------------*/
            if (!resJSONObject.containsKey("error")) {
                // 更新 token中的access_token 到token表
                String access_token = resJSONObject.getJSONObject("token").getString("access_token");
                String refresh_token = resJSONObject.getJSONObject("token").getString("refresh_token");
                Token token = new Token();
                token.setUser_id(user_id);
                token.setAccess_token(access_token);
                token.setRefresh_token(refresh_token);
                tokenMapper.updateToken(token);

                //验证成功 用户登录操作
                return login(jsonObject, user_id, access_token);
            } else {    //判断错误信息
                JSONObject errMesgOBJ = resJSONObject.getJSONObject("error");
                    if (errMesgOBJ != null && errMesgOBJ.containsKey("type")) {
                        // ERROR 验证码错误 | NOT_FOUND 用户不存在
                        String type = errMesgOBJ.getString("type");
                        if (type != null && !type.equals("") && type.equals("ERROR")) {
                            Map<String, String> errorMap = new HashMap<>();
                            errorMap.put("reason", "Verification code error");
                            return APIUtil.submitErrorResponse("验证码错误", errorMap);
                        } else if (type != null && !type.equals("") && type.equals("NOT_FOUND")) {
                            // 用户不存在时 新用户注册流程
                            return register(jsonObject, user_id, jsonInvite, attributeOBJ, invite_code);
                        }
                    }
                return APIUtil.submitErrorResponse("token获取失败", resJSONObject);
            }
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("reason", "Param missing local_token");
            return APIUtil.submitErrorResponse("用户local_token缺失", map);
        }
    }

    /**
     * 用户登录操作
     * @param jsonObject 请求参数
     * @param user_id   用户id
     * @param access_token 用户密匙
     * @return 登录返回参数
     */
    private APIResponse login(JSONObject jsonObject, Integer user_id, String access_token) {
        String loginStr = gson.toJson(jsonObject);
        HttpPost loginPost = new HttpPost(SF_LOGIN);
        loginPost.addHeader("PushEnvelope-Device-Token", access_token);
        String loginRespStr = APIPostUtil.post(loginStr, loginPost);

        JSONObject loginRespOBJ = JSONObject.fromObject(loginRespStr);
        if (loginRespOBJ.containsKey("error")) {
            return APIUtil.submitErrorResponse("LOGIN_ERROR", loginRespOBJ);
        } else {
            // 更新 uuid
            User user = userMapper.selectUserByUserId(user_id);
            if (user != null && (user.getUuid() == null || "".equals(user.getUuid()))) {
                user.setUuid(loginRespOBJ.getJSONObject("merchant").getString("uuid"));
                user.setMobile(loginRespOBJ.getJSONObject("merchant").getString("mobile"));
                userMapper.updateUser(user);
            }
        }
        return APIUtil.getResponse(SUCCESS, loginRespOBJ);
    }

    /**
     * 用户注册操作
     * @param jsonObject 请求参数
     * @param user_id   用户id
     * @param jsonInvite  邀请相关参数
     * @param attributeOBJ 邀请码参数
     * @param invite_code  邀请码
     * @return 注册返回参数
     */
    private APIResponse register(JSONObject jsonObject, Integer user_id, JSONObject jsonInvite, JSONObject attributeOBJ, String invite_code) {
        jsonObject.getJSONObject("merchant").put("attributes",attributeOBJ);
        jsonObject.getJSONObject("message").put("type","WX_REGISTER_VERIFY_SMS");

        //调用顺丰注册接口
        String registerUrl = gson.toJson(jsonObject);
        HttpPost registerPost = new HttpPost(SF_REGISTER_URL);
        String resp = APIPostUtil.post(registerUrl, registerPost);
        JSONObject registerResp = JSONObject.fromObject(resp);

        // 注册失败 匹配error
        if (registerResp.containsKey("error")) {
            return APIUtil.submitErrorResponse("注册失败", registerResp);
        } else {
            // 注册成功 获取邀请信息
            JSONObject merchantJSONObject = registerResp.getJSONObject("merchant");
            JSONObject tokenJSONObject = registerResp.getJSONObject("token");

            /*------------------------------------------------------- 存储用户邀请信息到 sftc_user_invite 表 -------------------------------------------------------------*/
            UserInvite invite = null;
            if (invite_code != null && !invite_code.equals("")) {
                String city = jsonInvite.getString("city");
                String channel = jsonInvite.getString("channel");
                invite = new UserInvite();
                invite.setUser_id(user_id);

                if (city != null && !"".equals(city))
                    invite.setCity(city);

                if (channel != null && !"".equals(channel))
                    invite.setInvite_channel(channel);
                invite.setInvite_code(invite_code);
                invite.setCreate_time(Long.toString(System.currentTimeMillis()));
                invite = userInviteDao.save(invite);
                this.logger.info("invite_id = " + invite.getId());
            }

            // 存储 token中的access_token 到token表
            Token token = new Token();
            token.setUser_id(user_id);
            token.setAccess_token(tokenJSONObject.getString("access_token"));
            token.setRefresh_token(tokenJSONObject.getString("refresh_token"));
            tokenMapper.updateToken(token);

            // 存储 merchant中的uuid 到user表
            User user = new User();
            user.setId(user_id);
            user.setMobile(merchantJSONObject.getString("mobile"));
            user.setUuid(merchantJSONObject.getString("uuid"));
            // 推荐注册
            if (invite != null) {
                user.setInvite_id(invite.getId());
            }
            userMapper.updateUser(user);

            return APIUtil.getResponse(SUCCESS, registerResp);
        }
    }

    /**
     * 发送微信模板消息的方法 下单成功后
     *
     * @param touser_id  接受折的id
     * @param messageArr 消息内容数据的数组
     * @param pagePath   跳转页面的路径
     */
    public void sendWXTemplateMessage(int touser_id, String[] messageArr, String pagePath, String form_id, String template_id) {
        User user = userMapper.selectUserByUserId(touser_id);

        // 构造 data 的数据体
        JSONObject dataBody = new JSONObject();
        for (int i = 0; messageArr.length > i; i++) {
            System.out.println(messageArr[i]);
            JSONObject keyword = new JSONObject();
            keyword.put("value", messageArr[i]);
            keyword.put("color", "#666666");
            dataBody.put("keyword" + (i + 1), keyword);
        }

        // 构造模板消息数据
        JSONObject messageBody = new JSONObject();
        messageBody.put("touser", user.getOpen_id());
        messageBody.put("template_id", template_id);
        messageBody.put("page", pagePath);
        messageBody.put("form_id", form_id);
        messageBody.put("emphasis_keyword", "keyword1.DATA");
        messageBody.put("data", dataBody.toString());
        String postStr = messageBody.toString();
        String postURL = WX_SEND_MESSAGE_PATH + WX_ACCESS_TOKEN;
        HttpPost httpPost = new HttpPost(postURL);
        String resultStr = APIPostUtil.post(postStr, httpPost);
        JSONObject resultJSONObject = JSONObject.fromObject(resultStr);
        if (resultJSONObject.containsKey("errcode") && resultJSONObject.getInt("errcode") != 0) {
            logger.error("---微信模板返回错误信息---" + resultStr);
        } else {
            logger.info("---微信模板返回成功信息----" + resultStr);
        }
    }
}
