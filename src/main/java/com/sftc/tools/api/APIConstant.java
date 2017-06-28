package com.sftc.tools.api;

public class APIConstant {

    /** 同城根地址 */
    private static final String SF_SAME_DOMAIN = "http://api-dev.sf-rush.com/";
    /** 大网根地址 */
    private static final String SF_NATION_DOMAIN = "http://api-c.sf-rush.com/api/sforderservice/";    // 正式环境
//    private static final String SF_NATION_DOMAIN = "http://api-c-test.sf-rush.com/api/sforderservice/"; // dev

    // 七牛相关
    /** accessKey */
    public static final String QN_ACCESSKEY = "Jyi6Ntprm38nI6n1heGjwXyQmzie8ZjY7l9Cq_Je";
    /** secretKey */
    public static final String QN_SECRETKEY = "eBqe82USZuB6gnqvjTsyg_qki6C6HwnkFIvNIH";
    /** bucket */
    public static final String QN_BUCKET = "sftc";

    // 微信
    /** appid */
    private static final String WX_APPID = "wxb6cbb81471348fec";
    /** secret */
    private static final String WX_SECRET = "b201962b8a3da757c72a0747eb6f1110";
    /** authorization */
    public static final String WX_AUTHORIZATION = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WX_APPID + "&secret=" + WX_SECRET + "&grant_type=authorization_code&js_code=";

    // 腾讯地图相关
    /** 地址解析接口地址 */
    public static final String MAP_GEOCODER_URL = "http://apis.map.qq.com/ws/geocoder/v1/?address=";
    /** App Key */
    public static final String MAP_GEOCODER_KEY = "ZZABZ-SLCWG-HV4Q4-IJYLH-LLBD6-V3FPR";

    // 同城用户相关
    /** 获取验证码 */
    public static final String SF_TAKE_MESSAGE_URL = SF_SAME_DOMAIN + "messages";
    /** 获取token */
    public static final String SF_GET_TOKEN = SF_SAME_DOMAIN + "merchants/me/token";
    /** 用户注册 */
    public static final String SF_REGISTER_URL = SF_SAME_DOMAIN + "merchants";
    /** 用户登录 */
    public static final String SF_LOGIN = SF_SAME_DOMAIN + "merchants/me";

    // 同城订单相关
    /** 优惠券 */
    public static final String SF_QUOTES_URL = SF_SAME_DOMAIN + "quotes";
    /** 通用订单请求地址 */
    public static final String SF_REQUEST_URL = SF_SAME_DOMAIN + "requests";
    /** 获取常量 */
    public static final String SF_CONSTANTS_URL = SF_SAME_DOMAIN + "constants/";

    // 大网订单相关
    /** 创建订单 */
    public static final String SF_CREATEORDER_URL = SF_NATION_DOMAIN + "ordercreate";
    /** 计价 */
    public static final String SF_COUNT_PRICE = SF_NATION_DOMAIN + "OrderFreightQuery";
    /** 路由地址 */
    public static final String SF_ORDERROUTE_URL = SF_NATION_DOMAIN + "OrderRouteQuery?orderid=";

}
