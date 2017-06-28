package com.sftc.web.service;

import com.google.gson.Gson;
import com.sftc.tools.api.AIPPost;
import com.sftc.tools.api.APIRequest;
import com.sftc.tools.api.APIResponse;
import com.sftc.web.controller.api.OrderController;
import com.sftc.web.model.OrderExpress;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.HttpPut;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

import static com.sftc.tools.api.APIConstant.SF_REQUEST_URL;

/**
 * Created by IntelliJ IDEA.
 *
 * @author _KeMing
 * @version 1.0
 * @Package com.sftc.web.service
 * @Description:
 * @date 2017/5/15
 * @Time 上午2:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:spring/spring-dao.xml"})
@ContextConfiguration(locations = {"classpath*:spring/spring**.xml"})
public class OrderServiceTest {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderController orderController;

    APIRequest request;

    @Before
    public void setUp() throws Exception {
        request = new APIRequest();
    }

    @Test
    public void placeOrder() throws Exception {

    }

    @Test
    public void payOrder() throws Exception {

    }

    @Test
    public void friendFillOrder() throws Exception {

    }


    /**
    *@Author:hxy starmoon1994
    *@Description: 取消订单 的测试方法
    *@Date:18:20 2017/6/24
    */
    @Test
    public  void deleteOrder() throws Exception {
        String s ="{" +
                "\"access_token\": \"f2c4934a8007eea56f5587\",\"event\": {" +
                "\"type\": \"dsad\"," +
                "\"source\": \"dsad\"" +
                "}," +
                "\"order_id\": \"1199\"" +
                "}";
        APIResponse apiResponse = orderService.deleteOrder(s);
        System.out.println(apiResponse);
    }
    /**
    *@Author:hxy starmoon1994
    *@Description: /order/senderplace 寄件人填写
    *@Date:18:21 2017/6/24
    */
    public void  senderplace() throws Exception {
        //从小幺鸡上直接粘贴 自动转义
        String s = "{\n" +
                "\"sender_name\": \"杨啟源\",\n" +
                "\"sender_mobile\": \"18124033797\",\n" +
                "\"sender_province\": \"广东省\",\n" +
                "\"sender_city\": \"深圳市\",\n" +
                "\"sender_area\": \"龙岗区\",\n" +
                "\"sender_addr\": \"大运\",\n" +
                "\"image\": \"yqy.jpg\",\n" +
                "\"voice\": \"你好\",\n" +
                "\"voice_time\": \"123\",\n" +
                "\"pay_method\": \"到付\",\n" +
                "\"distribution_method\": \"das\",\n" +
                "\"word_message\": \"你好\",\n" +
                "\"package_count\": 2,\n" +
                "\"gift_card_id\": 1,\n" +
                "\"package_type\": \"dsa\",\n" +
                "\"object_type\": \"dsa\",\n" +
                "\"sender_user_id\": 8,\n" +
                "\"order_type\": \"ORDER_MYSTERY\",\n" +
                "\"longitude\": 114.260976,\n" +
                "\"latitude\": 22.723223\n" +
                "}";
    }


    @Test
    public void evaluateTest(){
        // 生成 顺丰订单评价接口 需要的信息
        Object object = new String("{" +
                "\"request\": {" +
                "\"access_token\": \"7nWq8uExhVUoE7EW4ud2\"," +
                "\"uuid\": \"2c9a85895c549ce5015c58c16c6a10c9\"," +
                "\"attributes\": {" +
                "\"merchant_comments\": \"感觉这产品还不错\"," +
                "\"merchant_score\": \"5\"," +
                "\"merchant_tags\": {" +
                "\"deliver_speed\": \"0\"," +
                "\"service_attitude\": \"0\"," +
                "\"door_speed\": \"0\"" +
                "}" +
                "}," +
                "\"order_id\": \"1000\"" +
                "}" +
                "}");
        String str = new Gson().toJson(object);
        JSONObject jsonObject = null;
        JSONObject jsonObjectParam = JSONObject.fromObject(object);
        JSONObject request = jsonObjectParam.getJSONObject("request");
        JSONObject attributes = jsonObjectParam.getJSONObject("request").getJSONObject("attributes");
        int order_id = request.getInt("order_id");
        System.out.println("-   -order_id是："+order_id);
        // 向顺丰的接口发送评价信息
        String pay_url = SF_REQUEST_URL + "/" + request.get("uuid") + "/attributes/merchant_comment";
        HttpPut put = new HttpPut(pay_url);
        put.addHeader("PushEnvelope-Device-Token", (String) request.get("access_token"));
        String res = AIPPost.getPost(str, put);
//        jsonObject = JSONObject.fromObject(res);
        System.out.println("-   -这是res"+res);
    }


}