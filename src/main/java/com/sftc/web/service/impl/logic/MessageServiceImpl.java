package com.sftc.web.service.impl.logic;

import com.google.gson.Gson;
import com.sftc.tools.api.APIPostUtil;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.tools.api.APIUtil;
import com.sftc.tools.sf.SFTokenHelper;
import com.sftc.tools.token.TokenUtils;
import com.sftc.web.dao.jpa.UserInviteDao;
import com.sftc.web.dao.mybatis.TokenMapper;
import com.sftc.web.dao.mybatis.UserInviteMapper;
import com.sftc.web.dao.mybatis.UserMapper;
import com.sftc.web.model.Token;
import com.sftc.web.model.User;
import com.sftc.web.model.UserInvite;
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
import static com.sftc.tools.constant.WXConstant.WX_ACCESS_TOKEN;
import static com.sftc.tools.constant.WXConstant.WX_SEND_MESSAGE_PATH;
@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Resource
    private UserInviteMapper userInviteMapper;

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
        ////参数处理
//        String str = gson.toJson(apiRequest.getRequestParam());

        Object requestParam = apiRequest.getRequestParam();

        JSONObject jsonObject = JSONObject.fromObject(requestParam);  //调用顺丰注册接口的请求参数
        JSONObject jsonDevice = JSONObject.fromObject(requestParam); //调用顺丰获取设备的请求参数


        //调用顺丰获取设备参数整理
        jsonDevice .remove("message");
        jsonDevice .remove("invite");
        jsonDevice.put("type","WXC");

        //调用顺丰设备接口
        HttpPost postDevice = new HttpPost(SF_DEVICE_URL);
        postDevice.addHeader("PushEnvelope-Device-Token", SFTokenHelper.COMMON_ACCESSTOKEN);
        String resDevice = APIPostUtil.post(gson.toJson(jsonDevice), postDevice);
        JSONObject resJSONDevice = JSONObject.fromObject(resDevice);
        this.logger.info("调用顺丰设备接口返回的参数-----"+ resJSONDevice);
       if(resJSONDevice.containsKey("error")) {
            return APIUtil.submitErrorResponse("调用设备接口错误", resJSONDevice);
        }

        String deviceId =  resJSONDevice.getString("uuid");
       // String deviceId = "";

       if (!resJSONDevice.containsKey("uuid")) return APIUtil.paramErrorResponse("Parameter_Missing");
        if (!(resJSONDevice.getJSONObject("message").getString("mobile").length() == 11))
            return APIUtil.paramErrorResponse(" Mobile's length is not 11");

        //处理captcha相关的验证码
        if (resJSONDevice.containsKey("captcha")) {
            if (!jsonObject.getJSONObject("captcha").containsKey("uuid") || !resJSONDevice.getJSONObject("captcha").containsKey("content"))
                return APIUtil.paramErrorResponse("Parameter_Missing in captcha");
            String content = resJSONDevice.getJSONObject("captcha").getString("content");
            String uuid = resJSONDevice.getJSONObject("captcha").getString("uuid");
            if (uuid.length() < 4 || content.length() < 4)
                return APIUtil.paramErrorResponse("length is wrong in captcha");
        }
        //顺丰验证码请求参数准备
        jsonObject .remove("device");
        jsonObject .remove("invite");
        String mobile =  jsonObject.getJSONObject("message") .getString("mobile");
        jsonObject.getJSONObject("message").remove("mobile");
        jsonObject.getJSONObject("message").put("receiver",mobile);
        jsonObject.getJSONObject("message").put("type","WX_REGISTER_VERIFY_SMS");

        //请求顺丰验证码接口
        HttpPost post = new HttpPost(SF_TAKE_MESSAGE_URL);
        post.addHeader("PushEnvelope-Device-ID", deviceId);
        String res = APIPostUtil.post(jsonObject.toString(), post);
        JSONObject resultObject = JSONObject.fromObject(res);
        if (resultObject.containsKey("errors") || resultObject.containsKey("error"))
            return APIUtil.submitErrorResponse("SMS_Failed", resultObject);


        return APIUtil.getResponse(SUCCESS, resultObject);
    }

    /**
     * 用户注册
     */
    public APIResponse register(APIRequest apiRequest) {
        Object requestParam = apiRequest.getRequestParam();
        Integer user_id =  TokenUtils.getInstance().getUserId(tokenMapper);
        this.logger.info("-------user_id " + user_id);
        JSONObject jsonObject = JSONObject.fromObject(requestParam);  //调用顺丰注册接口的请求参数
        JSONObject jsonInvite = jsonObject.getJSONObject("invite");  //用户注册时 相关邀请信息
        String invite_code = jsonObject.getJSONObject("merchant").getJSONObject("attributes").getString("invite_code");

        jsonObject.remove("invite");

        // 调用顺丰接口
        String str = gson.toJson(jsonObject);
        HttpPost post = new HttpPost(SF_REGISTER_URL);
        String res = APIPostUtil.post(str, post);
        JSONObject resJSONObject = JSONObject.fromObject(res);

        // 注册失败 匹配error
        if (resJSONObject.containsKey("error")) {
            return APIUtil.submitErrorResponse("注册失败", resJSONObject);
        } else {

            // 注册成功
            JSONObject merchantJSONObject = resJSONObject.getJSONObject("merchant");
            JSONObject tokenJSONObject = resJSONObject.getJSONObject("token");


            UserInvite invite = null;
            if(!jsonInvite.getString("referrer_code").equals("0")){
                 invite = new UserInvite();
                invite.setUser_id(user_id);
                invite.setCity(jsonInvite.getString("city"));
                invite.setInvite_channel(jsonInvite.getInt("channel"));
                invite.setInvite_code(invite_code);
                invite.setCreate_time(Long.toString(System.currentTimeMillis()));
                invite  = userInviteDao.save(invite);
                this.logger.info("invite_id= " + invite.getId());
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
            //推荐注册
          if(invite != null ){
              user.setInvite_id(invite.getId());
           }
            userMapper.updateUser(user);

            //存储用户邀请的信息到user_invite表

            return APIUtil.getResponse(SUCCESS, resJSONObject);
       }



    }

    /**
     * 获取顺丰token 需要传入手机号和验证码
     */
    public APIResponse getToken(APIRequest apiRequest) {

        JSONObject jsonObject = JSONObject.fromObject(apiRequest.getRequestParam());
        
        if (jsonObject.containsKey("user_id")) {
            int user_id = jsonObject.getInt("user_id");
            String mobile = jsonObject.getJSONObject("merchant").getString("mobile");
            jsonObject.remove("user_id");

            // 验证手机号与user_id的匹配

            User userByPhone = userMapper.selectUserByPhone(mobile);
            User userByUserId = userMapper.selectUserByUserId(user_id);
            Map<String, String> map = new HashMap<String, String>(1, 1);
            if (userByUserId.getMobile() == null || "".equals(userByUserId.getMobile())) {
                // 用户的手机号为空 则 判断 参数手机号是否被用过
                if (userByPhone != null) {
                    map.put("name", userByPhone.getName());
                    return APIUtil.submitErrorResponse("手机号已被使用", map);
                }
            } else {// 用户已经绑定过手机号
                if (!mobile.equals(userByUserId.getMobile())) {
                    // 当 该id的用户手机号和参数手机号匹配
                    map.put("mobile", userByUserId.getMobile());
                    return APIUtil.submitErrorResponse("已注册", map);
                }
            }

            // 生成sf获取token的链接
            StringBuilder postUrl = new StringBuilder(SF_GET_TOKEN);
            if (jsonObject.containsKey("refresh_token") && !"".equals(jsonObject.get("refresh_token"))) {
                postUrl.append("?refresh_token=");
                postUrl.append(jsonObject.get("refresh_token"));
            }
            // 去除无用的json节点
            jsonObject.remove("refresh_token");

            // 调用顺丰接口
            String str = gson.toJson(jsonObject);
            HttpPost post = new HttpPost(postUrl.toString());
            String res = APIPostUtil.post(str, post);
            JSONObject resJSONObject = JSONObject.fromObject(res);

            if (!resJSONObject.containsKey("error")) {
                // 更新 token中的access_token 到token表
                String access_token = resJSONObject.getJSONObject("token").getString("access_token");
                String refresh_token = resJSONObject.getJSONObject("token").getString("refresh_token");
                Token token = new Token();
                token.setUser_id(user_id);
                token.setAccess_token(access_token);
                token.setRefresh_token(refresh_token);
                tokenMapper.updateToken(token);
                return APIUtil.getResponse(SUCCESS, resJSONObject);
            } else {
                return APIUtil.submitErrorResponse("token获取失败", resJSONObject);
            }
        } else {
            return APIUtil.paramErrorResponse("缺少参数，user_id");
        }
    }

    /**
     * 登陆 专指登陆顺丰的接口
     */
    public APIResponse sfLogin(APIRequest apiRequest) {

        JSONObject jsonObject = JSONObject.fromObject(apiRequest.getRequestParam());
        int user_id = jsonObject.getInt("user_id");
        String sfToken = jsonObject.getString("token");
        jsonObject.remove("user_id");
        jsonObject.remove("token");

        if (sfToken != null) {
            //调用顺丰的登陆接口
            String str = gson.toJson(jsonObject);
            HttpPost post = new HttpPost(SF_LOGIN);
            post.addHeader("PushEnvelope-Device-Token", sfToken);
            String res = APIPostUtil.post(str, post);

            JSONObject resJSONObject = JSONObject.fromObject(res);
            if (resJSONObject.containsKey("error")) {
                return APIUtil.submitErrorResponse("LOGIN_ERROR", resJSONObject);
            } else {
                // 更新 uuid
                User user = userMapper.selectUserByUserId(user_id);
                if (user != null && (user.getUuid() == null || "".equals(user.getUuid()))) {
                    user.setUuid(resJSONObject.getJSONObject("merchant").getString("uuid"));
                    user.setMobile(resJSONObject.getJSONObject("merchant").getString("mobile"));
                    userMapper.updateUser(user);
                }
            }
            return APIUtil.getResponse(SUCCESS, resJSONObject);
        } else {
            return APIUtil.paramErrorResponse("缺少参数sfToken");
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
            logger.error(resultStr);
        } else {
            logger.info(resultStr);
        }
    }

    /**
     * 获取图片验证码
     *
     * @return APIResponse
     */
    public APIResponse captchas() {
        // 接口要求传空json
        String jsonParam = "{   }";
        String postURL = SF_TAKE_CAPTCHAS_MESSAGE_URL;
        HttpPost httpPost = new HttpPost(postURL);
        String resultStr = APIPostUtil.post(jsonParam, httpPost);
        JSONObject resultJSONObject = JSONObject.fromObject(resultStr);
        if (resultJSONObject.containsKey("error") || resultJSONObject.containsKey("errors"))
            return APIUtil.submitErrorResponse("GET_SMS_CAPTCHAS_Failed", resultJSONObject);
        return APIUtil.getResponse(SUCCESS, resultJSONObject);
    }
}
