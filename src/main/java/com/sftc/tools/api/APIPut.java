package com.sftc.tools.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/6/1.
 */
public class APIPut {
    public static String getPut( HttpPut put){
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder sBuilder = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();

           // put.addHeader("username",username);
           // put.addHeader("password",password);


            HttpResponse httpResponse = httpClient.execute(put);
System.out.println(httpResponse.getStatusLine().getStatusCode());
            //连接成功

                System.out.println(httpClient);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                br = new BufferedReader(new InputStreamReader(is));
                String tempStr;
                sBuilder = new StringBuilder();
                while ((tempStr = br.readLine()) != null) {
                    sBuilder.append(tempStr);
                }
                System.out.println(sBuilder.toString());
                br.close();
                is.close();

        }catch (Exception e){
            System.out.println(e.fillInStackTrace());
            e.printStackTrace();
        }
        return sBuilder.toString();
    }
}
