package com.sftc.tools.constant;

/**
 * Author:hxy starmoon1994
 * Description: 微信 小程序服务通知 相关链接与常量
 * Date:15:25 2017/7/17
 */
public class WXConstant {
    ///////////////////秘钥相关/////////////////
    /**
     * ACCESS_TOKEN
     * 发送模板消息所需要是用的token，每两个小时刷新一次
     * 暂时不设置成final
     */
    public static String WX_ACCESS_TOKEN = "Jyi6Ntprm38nI6n1heGjwXyQmzie8ZjY7l9Cq_Je";
    /**
     * 第三方用户唯一凭证
     */
    public static String WX_APPID = "";
    /**
     * 第三方用户唯一凭证密钥，即appsecret
     */
    public static String WX_SECRET = "";

    ///////////////////模板相关/////////////////
    /**
     * 消息模板编号 1
     */
    public static String WX_template_id_1 = "";
    /**
     * 消息模板的跳转页面 1
     */
    public static String WX_template_page_1 = "";

    ///////////////////接口地址url/////////////////
    /**
     * 微信模板消息 接口地址
     */
    public static String WX_SEND_MESSAGE_PATH = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";
    /**
     * 获取 access_token 接口地址
     */
    public static String WX_GET_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";


}
