package com.sftc.tools.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Administrator
 */
public class ApiPostUtil {

    private static final String CONTENT_TYPE = "application/json";

    @SuppressWarnings("resource")
    public static String post(String json, HttpEntityEnclosingRequestBase post) {

        post.setHeader("Content-Type", CONTENT_TYPE);

        StringEntity s = new StringEntity(json, "utf-8");
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE));
        post.setEntity(s);

        HttpClient client = new DefaultHttpClient();
        StringBuilder sb = new StringBuilder();
        String result = "";

        try {
            HttpResponse httpResponse = client.execute(post);
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            inStream.close();

            result = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}

