package com.sftc.tools.api;

import com.sftc.web.model.Order;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2017/6/14.
 */
public class ValidateNull {
    public static void validateParam(JSONObject param,Order order) {
        if(param.getJSONObject("order").get("voice_time")==null){
            order.setVoice_time(0);
        }else {
            order.setVoice_time(Integer.parseInt((String)param.getJSONObject("order").get("voice_time")));
        }if(param.getJSONObject("order").get("gift_card_id")==null){
            order.setGift_card_id(0);
        }else{
            order.setGift_card_id(Integer.parseInt((String) param.getJSONObject("order").get("gift_card_id")));
        }if(param.getJSONObject("order").get("word_message")==null){
            order.setWord_message("");
        }else {
            order.setWord_message((String) param.getJSONObject("order").get("word_message"));
        }if(param.getJSONObject("order").get("voice")==null){
            order.setVoice("");
        }else {
            order.setVoice((String) param.getJSONObject("order").get("voice"));
        }if(param.getJSONObject("order").get("image")==null){
            order.setImage("");
        }else {
            order.setImage((String) param.getJSONObject("order").get("image"));
        }
    }
    public static void validateParamT(JSONObject param,Order order) {
        if(param.getJSONObject("request").getJSONObject("order").get("voice_time")==null){
            order.setVoice_time(0);
        }else {
            order.setVoice_time(Integer.parseInt((String)param.getJSONObject("request").getJSONObject("order").get("voice_time")));
        }if(param.getJSONObject("request").getJSONObject("order").get("gift_card_id")==null){
            order.setGift_card_id(0);
        }else{
            order.setGift_card_id(Integer.parseInt((String) param.getJSONObject("request").getJSONObject("order").get("gift_card_id")));
        }if(param.getJSONObject("request").getJSONObject("order").get("word_message")==null){
            order.setWord_message("");
        }else {
            order.setWord_message((String) param.getJSONObject("request").getJSONObject("order").get("word_message"));
        }if(param.getJSONObject("request").getJSONObject("order").get("voice")==null){
            order.setVoice("");
        }else {
            order.setVoice((String) param.getJSONObject("request").getJSONObject("order").get("voice"));
        }
        if (param.getJSONObject("request").getJSONObject("order").get("image")==null){
            order.setImage("");
        }else {
            order.setImage((String) param.getJSONObject("request").getJSONObject("order").get("image"));
        }
    }
}
