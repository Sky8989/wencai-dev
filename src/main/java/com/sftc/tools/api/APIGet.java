package com.sftc.tools.api;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.NameValuePair;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */
public class APIGet {
    public static String getGet( HttpGet get) {
        //httpClient
        HttpClient httpClient = new DefaultHttpClient();



        //response
        HttpResponse response = null;
        try{
            response = httpClient.execute(get);
        }catch (Exception e) {}

        //get response into String
        String temp="";
        try{
            HttpEntity entity = response.getEntity();
            temp=EntityUtils.toString(entity,"UTF-8");
        }catch (Exception e) {}

        return temp;
    }
}

