package com.sftc.tools.constant;

import com.sftc.tools.api.ApiGetUtil;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Component;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.sftc.tools.constant.ThirdPartyConstant.WX_APPID;
import static com.sftc.tools.constant.ThirdPartyConstant.WX_SECRET;

/**
 * Author:hxy starmoon1994
 * Description: 微信 小程序服务通知 相关链接与常量
 * Date:15:25 2017/7/17
 *
 * @author Administrator
 */
@Component
public class WxConstant {
    ///////////////////秘钥相关/////////////////
    /**
     * ACCESS_TOKEN
     * 发送模板消息所需要是用的token，每3分钟刷新一次
     * 暂时不设置成final
     */
    public static String WX_ACCESS_TOKEN = "Jyi6Ntprm38nI6n1heGjwXyQmzie8ZjY7l9Cq_Je";
    /**
     * 第三方用户唯一凭证
     */
    private static final String WX_APPID_TEST = "wx4a6fc8541558c180";
    /**
     * 第三方用户唯一凭证密钥，即appsecret
     */
    public static final String WX_SECRET_TEST = "ed327e274f47fab663f9edd4ba10054b";

    ///////////////////模板相关/////////////////
    /**
     * 消息模板编号 1
     * near的模板
     */
    public static final String WX_TEMPLATE_ID1 = "80Xh6eqJUsG4G2J_O995H4qTnENtFXWL5uxB9eOGwaI";
    /**
     * hxy的模板
     */
    public static final String WX_TEMPLATE_ID1_TEST = "XdOTHkqulof0zT7zAkyPRIcfv2g2Kz_SEMmz5_aOvhc";
    /**
     * 消息模板的跳转页面 1
     */
    public static final String WX_TEMPLATE_PAGE1 = "";

    ///////////////////接口地址url/////////////////
    /**
     * 微信模板消息 接口地址
     */
    public static final String WX_SEND_MESSAGE_PATH = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";
    /**
     * 获取 access_token 接口地址
     */
    public static final String WX_GET_ACCESSTOKEN_TEST = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WX_APPID_TEST + "&secret=" + WX_SECRET_TEST;
    public static final String WX_GET_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WX_APPID + "&secret=" + WX_SECRET;

    ///////////////////记录相关/////////////////
    /**
     * 记录属性token的次数
     */
    public static int count = 0;

    ///////////////////自动刷新/////////////////
    static {
        long delay = 0;
        long period = 180;
        final TimerTask refreshAccessTokenTask = new TimerTask() {
            @Override
            public void run() {
                // 操作acceee_token的获取链接
                HttpGet httpGet = new HttpGet(WX_GET_ACCESSTOKEN);
                String resultStr = ApiGetUtil.get(httpGet);
                JSONObject resultObject = JSONObject.fromObject(resultStr);
                if (resultObject.containsKey("access_token")) {
                    WX_ACCESS_TOKEN = resultObject.getString("access_token");
                    System.out.println("刷新微信模板消息 access_token: " + WX_ACCESS_TOKEN);
                } else {
                    System.out.println("刷新微信模板消息 access_token 失败: " + resultStr);
                }
            }
        };
        ScheduledExecutorService refreshSES = Executors.newScheduledThreadPool(1);
        refreshSES.scheduleAtFixedRate(refreshAccessTokenTask, delay, period, TimeUnit.SECONDS);
    }
}
