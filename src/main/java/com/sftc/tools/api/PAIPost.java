package com.sftc.tools.api;

import com.google.gson.Gson;
import com.sftc.web.model.wechat.WechatUser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.map.util.JSONPObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Administrator on 2017/5/15.
 */
public class PAIPost {
    public static String getPost(Object json,String URL) {

                 HttpClient client = new DefaultHttpClient();
                 HttpPost post = new HttpPost(URL);

                 post.setHeader("Content-Type", "application/json");
                 post.addHeader("Authorization", "Basic YWRtaW46");
                 String result = "";

                 try {

                         StringEntity s = new StringEntity(json.toString(), "utf-8");
                         s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                                         "application/json"));
                         post.setEntity(s);

                         // 发送请求
                         HttpResponse httpResponse = client.execute(post);
                        System.out.println(json.toString());
                         // 获取响应输入流
                         InputStream inStream = httpResponse.getEntity().getContent();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(
                                         inStream, "utf-8"));
                         StringBuilder strber = new StringBuilder();
                         String line = null;
                         while ((line = reader.readLine()) != null)
                                 strber.append(line + "\n");
                         inStream.close();

                         result = strber.toString();
                         System.out.println(result);

                         if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                                     System.out.println("请求服务器成功，做相应处理");

                            } else {

                                 System.out.println("请求服务端失败");

                             }


                     } catch (Exception e) {
                         System.out.println("请求异常");
                         throw new RuntimeException(e);
                     }

                return result;
             }
    }

