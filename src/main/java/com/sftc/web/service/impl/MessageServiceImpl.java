package com.sftc.web.service.impl;

import com.google.gson.Gson;
import com.sftc.tools.api.ApiPostUtil;
import com.sftc.tools.api.ApiResponse;
import com.sftc.tools.api.ApiUtil;
import com.sftc.tools.constant.CustomConstant;
import com.sftc.tools.sf.SfTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.UserInviteDao;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.entity.Token;
import com.sftc.web.model.entity.User;
import com.sftc.web.model.entity.UserInvite;
import com.sftc.web.model.vo.swaggerRequest.*;
import com.sftc.web.service.MessageService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.sftc.tools.api.ApiStatus.SUCCESS;
import static com.sftc.tools.constant.SfConstant.*;
import static com.sftc.tools.constant.SfConstant.SF_TAKE_CAPTCHAS_MESSAGE_URL;
import static com.sftc.tools.constant.WxConstant.WX_ACCESS_TOKEN;
import static com.sftc.tools.constant.WxConstant.WX_SEND_MESSAGE_PATH;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private TokenMapper tokenMapper;
    @Resource
    private UserInviteDao userInviteDao;

    @Resource
    private UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取短信验证码
     */
    @Override
    public ApiResponse getMessage(SMSMessageRequestVO apiRequest) {
        if (apiRequest == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "请求体为空");
        }
        // 调用顺丰注册接口的请求参数
        JSONObject jsonObject = JSONObject.fromObject(apiRequest);
        // 调用顺丰获取设备的请求参数
        JSONObject jsonDevice = JSONObject.fromObject(apiRequest);
        String mobile = jsonObject.getJSONObject("message").getString("mobile");
        String deviceStr = "device";
        // 调用顺丰获取设备接口的参数整理
        jsonDevice.remove("message");
        jsonDevice.remove("invite");
        jsonDevice.getJSONObject(deviceStr).put("type", "WXC");
        // 调用顺丰设备接口
        HttpPost postDevice = new HttpPost(SF_DEVICE_URL);
        postDevice.addHeader("PushEnvelope-Device-Token", SfTokenHelper.COMMON_ACCESSTOKEN);
        String resDevice = ApiPostUtil.post(new Gson().toJson(jsonDevice), postDevice);
        JSONObject resJSONDevice = JSONObject.fromObject(resDevice);
        // 处理获取顺丰设备ID接口的回调
        if (resJSONDevice.containsKey(CustomConstant.ERROR)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Getting the ID error of the small program device");
        }
        if (!resJSONDevice.containsKey(deviceStr)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Parameter device missing in sf-api callback.");
        }
        String uuidStr = "uuid";
        if (!resJSONDevice.getJSONObject(deviceStr).containsKey(uuidStr)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "Parameter device.uuid missing in sf-api callback.");
        }
        // 正常情况，添加设备ID到获取验证码的请求headers中
        String deviceId = resJSONDevice.getJSONObject(deviceStr).getString(uuidStr);

        // 顺丰验证码请求参数处理
        jsonObject.remove(deviceStr);
        jsonObject.remove("invite");
        jsonObject.getJSONObject("message").remove("mobile");
        jsonObject.getJSONObject("message").put("receiver", mobile);

        // 请求顺丰验证码接口
        HttpPost post = new HttpPost(SF_TAKE_MESSAGE_URL);
        post.addHeader("PushEnvelope-Device-ID", deviceId);
        String res = ApiPostUtil.post(jsonObject.toString(), post);
        JSONObject resultObject = JSONObject.fromObject(res);
        // 处理验证码结果
        if (resultObject.containsKey(CustomConstant.ERROR)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "请求验证码失败", resultObject.getJSONObject(CustomConstant.ERROR));
        }
        if (resultObject.containsKey(CustomConstant.ERRORS)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "请求验证码失败", resultObject.getJSONArray(CustomConstant.ERRORS));
        }

        return ApiUtil.getResponse(SUCCESS, resultObject);
    }

    /**
     * 用户注册
     */
    @Override
    public ApiResponse register(RegisterRequestVO apiRequest) {
        if (apiRequest == null) {
            return ApiUtil.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "请求体为空");
        }
        String userUUID = TokenUtils.getInstance().getUserUUID();
        this.logger.info("-------userUUID " + userUUID);
        //调用顺丰注册接口的请求参数
        JSONObject jsonObject = JSONObject.fromObject(apiRequest);
        //用户注册时 相关邀请信息
        JSONObject jsonInvite = jsonObject.getJSONObject("invite");

        String inviteCode = null;
        String merchantStr = "merchant";
        String attributesStr = "attributes";
        String inviteCodeStr = "invite_code";
        if (jsonObject.getJSONObject(merchantStr).getJSONObject(attributesStr).containsKey(inviteCodeStr)) {
            inviteCode = jsonObject.getJSONObject(merchantStr).getJSONObject(attributesStr).getString(inviteCodeStr);
        }
        this.logger.info("--- invite_code " + inviteCode);
        jsonObject.remove("invite");
        // 调用顺丰接口
        HttpPost post = new HttpPost(SF_REGISTER_URL);
        String res = ApiPostUtil.post(new Gson().toJson(jsonObject), post);
        JSONObject resJSONObject = JSONObject.fromObject(res);

        // 注册失败 匹配error
        if (resJSONObject.containsKey(CustomConstant.ERROR)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "注册失败", resJSONObject);
        } else {
            // 注册成功
            JSONObject merchantJSONObject = resJSONObject.getJSONObject(merchantStr);
            JSONObject tokenJSONObject = resJSONObject.getJSONObject(CustomConstant.HEARD_TOKEN);

            UserInvite invite = getUserInvite(userUUID, jsonInvite, inviteCode);

            // 存储 token中的access_token 到token表
            Token token = new Token();
            token.setUser_uuid(userUUID);
            token.setAccess_token(tokenJSONObject.getString(CustomConstant.ACCESS_TOKEN));
            token.setRefresh_token(tokenJSONObject.getString("refresh_token"));
            tokenMapper.updateToken(token);

            // 存储 merchant中的uuid 到user表
            User user = new User();
            user.setUuid(userUUID);
            user.setMobile(merchantJSONObject.getString("mobile"));
            user.setUuid(merchantJSONObject.getString("uuid"));

            userMapper.updateUser(user);

            // 存储用户邀请的信息到user_invite表
            return ApiUtil.getResponse(SUCCESS, resJSONObject);
        }
    }

    /**
     * 获取顺丰token 需要传入手机号和验证码
     */
    @Override
    public ApiResponse getToken(GetTokenRequestVO apiRequest) {

        JSONObject jsonObject = JSONObject.fromObject(apiRequest);
        String userUUID = TokenUtils.getInstance().getUserUUID();
        if (StringUtils.isNotBlank(userUUID)) {
            String mobile = jsonObject.getJSONObject("merchant").getString("mobile");

            // 验证手机号与user_uuid的匹配

            User userByPhone = userMapper.selectUserByPhone(mobile);
            User userByUserId = userMapper.selectUserByUserUUId(userUUID);
            Map<String, String> map = new HashMap<>(1, 1);
            if (userByUserId.getMobile() == null || "".equals(userByUserId.getMobile())) {
                // 用户的手机号为空 则 判断 参数手机号是否被用过
                if (userByPhone != null) {
                    map.put("name", userByPhone.getName());
                    return ApiUtil.error(HttpStatus.CONFLICT.value(), "手机号已被使用", map);
                }
            } else {// 用户已经绑定过手机号
                if (!mobile.equals(userByUserId.getMobile())) {
                    // 当 该id的用户手机号和参数手机号匹配
                    map.put("mobile", userByUserId.getMobile());
                    return ApiUtil.error(HttpStatus.CONFLICT.value(), "已注册", map);
                }
            }

            // 生成sf获取token的链接
            StringBuilder postUrl = new StringBuilder(SF_GET_TOKEN);
            if (jsonObject.containsKey(CustomConstant.REFRESH_TOKEN) && !"".equals(jsonObject.get(CustomConstant.REFRESH_TOKEN))) {
                postUrl.append("?refresh_token=");
                postUrl.append(jsonObject.get(CustomConstant.REFRESH_TOKEN));
            }
            // 去除无用的json节点
            jsonObject.remove("refresh_token");

            // 调用顺丰接口
            HttpPost post = new HttpPost(postUrl.toString());
            String res = ApiPostUtil.post(new Gson().toJson(jsonObject), post);
            JSONObject resJSONObject = JSONObject.fromObject(res);

            if (!resJSONObject.containsKey(CustomConstant.ERROR)) {
                // 更新 token中的access_token 到token表
                String accessToken = resJSONObject.getJSONObject(CustomConstant.HEARD_TOKEN).getString(CustomConstant.ACCESS_TOKEN);
                String refreshToken = resJSONObject.getJSONObject(CustomConstant.HEARD_TOKEN).getString(CustomConstant.REFRESH_TOKEN);
                Token token = new Token();
                token.setUser_uuid(userUUID);
                token.setAccess_token(accessToken);
                token.setRefresh_token(refreshToken);
                tokenMapper.updateToken(token);
                return ApiUtil.getResponse(SUCCESS, resJSONObject);
            } else {
                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "token获取失败", resJSONObject);
            }
        } else {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "缺少参数");
        }
    }

    /**
     * 登陆 专指登陆顺丰的接口
     */
    @Override
    public ApiResponse sfLogin(SFLoginRequestVO body) {

        JSONObject jsonObject = JSONObject.fromObject(body);
        String userUUID = TokenUtils.getInstance().getUserUUID();
        String sfToken = jsonObject.getString("token");
        jsonObject.remove("token");

        if (sfToken != null) {
            //调用顺丰的登陆接口
            String str = new Gson().toJson(jsonObject);
            HttpPost post = new HttpPost(SF_LOGIN);
            post.addHeader("PushEnvelope-Device-Token", sfToken);
            String res = ApiPostUtil.post(str, post);

            JSONObject resJSONObject = JSONObject.fromObject(res);
            if (resJSONObject.containsKey(CustomConstant.ERROR)) {
                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "LOGIN_ERROR", resJSONObject);
            } else {
                // 更新 uuid
                User user = userMapper.selectUserByUserUUId(userUUID);
                if (user != null && (StringUtils.isBlank(user.getUuid()))) {
                    user.setUuid(resJSONObject.getJSONObject("merchant").getString("uuid"));
                    user.setMobile(resJSONObject.getJSONObject("merchant").getString("mobile"));
                    userMapper.updateUser(user);
                }
            }
            return ApiUtil.getResponse(SUCCESS, resJSONObject);
        } else {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "缺少参数sfToken");
        }
    }

    /**
     * 验证短信验证码
     */
    @Override
    public ApiResponse messageCheck(UserValidateVO apiRequest) {

        JSONObject jsonObject = JSONObject.fromObject(apiRequest);
        String userUUID = TokenUtils.getInstance().getUserUUID();
        String merchantStr = "merchant";
        if (StringUtils.isNotBlank(userUUID)) {
            String mobile = jsonObject.getJSONObject(merchantStr).getString("mobile");

            // 验证手机号与user_uuid的匹配 根据手机号和 用户 id 查询用户
            User userByPhone = userMapper.selectUserByPhone(mobile);
            User userByUserId = userMapper.selectUserByUserUUId(userUUID);
            Map<String, String> map = new HashMap<>(1, 1);
            if (userByUserId.getMobile() == null || "".equals(userByUserId.getMobile())) {
                // 用户的手机号为空 则判断该手机号是否被使用
                if (userByPhone != null) {
                    map.put("name", userByPhone.getName());
                    return ApiUtil.error(HttpStatus.CONFLICT.value(), "该手机号已被使用", map);
                }
            } else {// 用户已经绑定手机号 验证手机号不为用户手机号
                if (!mobile.equals(userByUserId.getMobile())) {
                    map.put("mobile", userByUserId.getMobile());
                    return ApiUtil.error(HttpStatus.CONFLICT.value(), "该用户已验证手机", map);
                }
            }

           /*-------------------------------------- 调用顺丰获取 token 接口 验证手机验证码是否正确 ---------------------------------------*/
            //用户注册时 相关邀请信息
            JSONObject jsonInvite = jsonObject.getJSONObject("invite");
            JSONObject attributeOBJ = jsonObject.getJSONObject("merchant").getJSONObject("attributes");

            String inviteCode = null;
            String attributesStr = "attributes";
            String inviteCodeStr = "invite_code";
            if (jsonObject.getJSONObject(merchantStr).getJSONObject(attributesStr).containsKey(inviteCodeStr)) {
                inviteCode = jsonObject.getJSONObject(merchantStr).getJSONObject(attributesStr).getString(inviteCodeStr);
            }
            //移除获取token时不需要的参数
            jsonObject.getJSONObject(merchantStr).remove("attributes");
            jsonObject.remove("invite");

            // 生成sf获取token的链接

            // 调用顺丰获取token接口
            Gson gson = new Gson();
            String str = gson.toJson(jsonObject);
            HttpPost post = new HttpPost(SF_GET_TOKEN);
            String res = ApiPostUtil.post(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);

             /*-------------------------------------- 验证成功调用登录接口 失败若为 NOT_FOUND 调用注册接口 ---------------------------------------*/
            if (!resJSONObject.containsKey(CustomConstant.ERROR)) {
                // 更新 token中的access_token 到token表
                String accessToken = resJSONObject.getJSONObject(CustomConstant.HEARD_TOKEN).getString(CustomConstant.ACCESS_TOKEN);
                String refreshToken = resJSONObject.getJSONObject(CustomConstant.HEARD_TOKEN).getString(CustomConstant.REFRESH_TOKEN);
                Token token = new Token();
                token.setUser_uuid(userUUID);
                token.setAccess_token(accessToken);
                token.setRefresh_token(refreshToken);
                tokenMapper.updateToken(token);

                //调用顺丰登录接口
                String loginStr = gson.toJson(jsonObject);
                HttpPost loginPost = new HttpPost(SF_LOGIN);
                loginPost.addHeader("PushEnvelope-Device-Token", accessToken);
                String loginRespStr = ApiPostUtil.post(loginStr, loginPost);

                JSONObject loginRespOBJ = JSONObject.fromObject(loginRespStr);
                if (loginRespOBJ.containsKey(CustomConstant.ERROR)) {
                    return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "LOGIN_ERROR", loginRespOBJ);
                } else {
                    // 更新 uuid
                    User user = userMapper.selectUserByUserUUId(userUUID);
                    if (user != null && StringUtils.isBlank(user.getUuid())) {
                        user.setUuid(loginRespOBJ.getJSONObject(merchantStr).getString("uuid"));
                        user.setMobile(loginRespOBJ.getJSONObject(merchantStr).getString("mobile"));
                        userMapper.updateUser(user);
                    }
                }
                return ApiUtil.getResponse(SUCCESS, loginRespOBJ);
            } else {    //判断错误信息
                JSONObject errMesgOBJ = resJSONObject.getJSONObject("error");
                String typeStr = "type";
                if (errMesgOBJ != null && errMesgOBJ.containsKey(typeStr)) {
                    // ERROR 验证码错误 | NOT_FOUND 用户不存在
                    String type = errMesgOBJ.getString(typeStr);
                    String notFoundStr = "NOT_FOUND";
                    if (StringUtils.isNotBlank(type)) {
                        if (type.equals(CustomConstant.ERROR)) {
                            Map<String, String> errorMap = new HashMap<>(2);
                            errorMap.put("reason", "Verification code error");
                            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "验证码错误", errorMap);
                        } else if (StringUtils.equals(type, notFoundStr)) {
                            // 用户不存在时 新用户注册流程 添加所需参数
                            jsonObject.getJSONObject(merchantStr).put("attributes", attributeOBJ);
                            jsonObject.getJSONObject("message").put(typeStr, "WX_REGISTER_VERIFY_SMS");

                            //调用顺丰注册接口
                            String registerUrl = gson.toJson(jsonObject);
                            HttpPost registerPost = new HttpPost(SF_REGISTER_URL);
                            String resp = ApiPostUtil.post(registerUrl, registerPost);
                            JSONObject registerResp = JSONObject.fromObject(resp);

                            // 注册失败 匹配error
                            if (registerResp.containsKey(CustomConstant.ERROR)) {
                                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "验证码错误", registerResp);
                            } else {
                                // 注册成功 获取邀请信息
                                JSONObject merchantJSONObject = registerResp.getJSONObject(merchantStr);
                                JSONObject tokenJSONObject = registerResp.getJSONObject(CustomConstant.HEARD_TOKEN);

                                UserInvite invite = getUserInvite(userUUID, jsonInvite, inviteCode);

                                // 存储 token中的access_token 到token表
                                Token token = new Token();
                                token.setUser_uuid(userUUID);
                                token.setAccess_token(tokenJSONObject.getString("access_token"));
                                token.setRefresh_token(tokenJSONObject.getString("refresh_token"));
                                tokenMapper.updateToken(token);

                                // 存储 merchant中的uuid 到user表
                                User user = new User();
                                user.setUuid(userUUID);
                                user.setMobile(merchantJSONObject.getString("mobile"));
                                user.setUuid(merchantJSONObject.getString("uuid"));
                                userMapper.updateUser(user);

                                // 存储用户邀请的信息到user_invite表
                                return ApiUtil.getResponse(SUCCESS, registerResp);
                            }
                        }
                    }

                }
                return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "token获取失败", resJSONObject);
            }
        } else {
            Map<String, String> map = new HashMap<>(2);
            map.put("reason", "Param missing local_token");
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "用户local_token缺失", map);
        }
    }

    private UserInvite getUserInvite(String userUUID, JSONObject jsonInvite, String inviteCode) {
        if (StringUtils.isNotBlank(inviteCode)) {
            String city = jsonInvite.getString("city");
            String channel = jsonInvite.getString("channel");
            UserInvite invite = new UserInvite();
            invite.setUser_uuid(userUUID);

            if (StringUtils.isNotBlank(city)) {
                invite.setCity(city);
            }

            if (StringUtils.isNotBlank(channel)) {
                invite.setInvite_channel(channel);
            }
            invite.setInvite_code(inviteCode);
            invite.setCreate_time(Long.toString(System.currentTimeMillis()));
            invite = userInviteDao.save(invite);
            this.logger.info("invite_id = " + invite.getId());
            return invite;
        }
        return null;
    }

    /**
     * 发送微信模板消息的方法 下单成功后
     *
     * @param userUUID   接受折的id
     * @param messageArr 消息内容数据的数组
     * @param pagePath   跳转页面的路径
     */
    @Override
    public void sendWXTemplateMessage(String userUUID, String[] messageArr, String pagePath, String formId, String templateId) {
        User user = userMapper.selectUserByUserUUId(userUUID);

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
        messageBody.put("touser", user.getOpenId());
        messageBody.put("template_id", templateId);
        messageBody.put("page", pagePath);
        messageBody.put("form_id", formId);
        messageBody.put("emphasis_keyword", "keyword1.DATA");
        messageBody.put("data", dataBody.toString());
        String postStr = messageBody.toString();
        String postURL = WX_SEND_MESSAGE_PATH + WX_ACCESS_TOKEN;
        HttpPost httpPost = new HttpPost(postURL);
        String resultStr = ApiPostUtil.post(postStr, httpPost);
        JSONObject resultJSONObject = JSONObject.fromObject(resultStr);
        String errCodeStr = "errcode";
        if (resultJSONObject.containsKey(errCodeStr) && resultJSONObject.getInt(errCodeStr) != 0) {
            logger.error("---微信模板返回错误信息---" + resultStr);
        } else {
            logger.info("---微信模板返回成功信息----" + resultStr);
        }
    }

    /**
     * 获取图片验证码
     *
     * @return ApiResponse
     */
    @Override
    public ApiResponse captchas() {
        // 接口要求传空json
        String jsonParam = "{   }";
        String postURL = SF_TAKE_CAPTCHAS_MESSAGE_URL;
        HttpPost httpPost = new HttpPost(postURL);
        String resultStr = ApiPostUtil.post(jsonParam, httpPost);
        JSONObject resultJSONObject = JSONObject.fromObject(resultStr);
        if (resultJSONObject.containsKey(CustomConstant.ERROR) || resultJSONObject.containsKey(CustomConstant.ERRORS)) {
            return ApiUtil.error(HttpStatus.BAD_REQUEST.value(), "GET_SMS_CAPTCHAS_Failed", resultJSONObject);
        }
        return ApiUtil.getResponse(SUCCESS, resultJSONObject);
    }
}
