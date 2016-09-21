package com.bbd;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.net.SyslogAppender;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

/**
 * 模拟登陆
 * Created by bbd on 2016/9/21.
 */
public class LoginTest {

    @Test
    public void test() throws Exception {
        HttpClientBuilder builder = HttpClients.custom();
        CloseableHttpClient client = builder.build();
        HttpPost httpPost = new HttpPost("http://svn.club/user/login");
        ArrayList<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
        param.add(new BasicNameValuePair("uid","wangweislk"));
        param.add(new BasicNameValuePair("pwd","wangwei123456"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(param);
        httpPost.setEntity(entity);
        CloseableHttpResponse reponse = client.execute(httpPost);
        // 获取请求的状态吗
        int statusCode = reponse.getStatusLine().getStatusCode();
        if(statusCode == 302){
            Header[] locations = reponse.getHeaders("Location");
            String redirectUrl = "";
            if(locations.length>0){
                redirectUrl = locations[0].getValue();
                System.out.println(redirectUrl);
            }
            httpPost.setURI(new URI("http://svn.club"+redirectUrl));
            reponse = client.execute(httpPost);
            HttpEntity entity1 = reponse.getEntity();
            System.out.println(EntityUtils.toString(entity1));
        }



    }
}
