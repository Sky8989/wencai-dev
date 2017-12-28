package com.sftc.tools.constant;

/**
 * 第三方平台常量
 *
 * @author Administrator
 */
public class ThirdPartyConstant {

    /////////////// 七牛相关 ///////////////

    /**
     * accessKey
     */
    public static final String QN_ACCESSKEY = "Jyi6Ntprm38nI6n1heGjwXyQmzie8ZjY7l9Cq_Je";
    /**
     * secretKey
     */
    public static final String QN_SECRETKEY = "eBqe82USZuB6gnqvjTsyg_qki6C6HwnkFIvNIH-y";
    /**
     * bucket
     */
    public static final String QN_BUCKET = "sftc";
    /**
     * domain
     */
    public static final String QN_DOMAIN = "https://sf.dankal.cn/";

    /////////////// 微信相关 ///////////////

    /**
     * appid
     */
    public static final String WX_APPID = "wxb6cbb81471348fec";
    /**
     * secret
     */
    public static final String WX_SECRET = "b201962b8a3da757c72a0747eb6f1110";
    /**
     * authorization
     */
    public static final String WX_AUTHORIZATION = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WX_APPID + "&secret=" + WX_SECRET + "&grant_type=authorization_code&js_code=";


    /////////////// 腾讯地图相关 ///////////////
    /**
     * App Key
     */
    private static final String MAP_GEOCODER_KEY = "ZZABZ-SLCWG-HV4Q4-IJYLH-LLBD6-V3FPR";
    /**
     * 地址解析接口地址
     */
    public static final String MAP_GEOCODER_URL = "http://apis.map.qq.com/ws/geocoder/v1/?key=" + MAP_GEOCODER_KEY + "&address={address}";
    /**
     * 逆地址解析接口地址
     */
    public static final String MAP_REGEOCODER_URL = "http://apis.map.qq.com/ws/geocoder/v1/?key=" + MAP_GEOCODER_KEY + "&location={location}&get_poi=1&poi_options={poi_options}";
    /**
     * 距离计算
     */
    public static final String MAP_ADDRESS_DISTANCE_URL = "http://apis.map.qq.com/ws/distance/v1/?key=" + MAP_GEOCODER_KEY + "&from={from}&to={to}";
    public static final String MAP_ADDRESS_DISTANCE_URL_2 = "http://apis.map.qq.com/ws/direction/v1/driving/?key=" + MAP_GEOCODER_KEY + "&policy=REAL_TRAFFIC&from={from}&to={to}";
}
