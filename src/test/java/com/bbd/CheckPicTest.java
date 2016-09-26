package com.bbd;

import com.bbd.utils.PageUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by bbd on 2016/9/25.
 */
public class CheckPicTest {

    /**
     * xpath问题，需要手写
     * http://s.hc360.com/?w=%B3%F7%B9%F1&mc=seller
     * @throws Exception
     */
//	@Test
    public void test1() throws Exception {
        String content = PageUtils.getContent("http://s.hc360.com/?w=%B3%F7%B9%F1&mc=seller");
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(content);
        Object[] evaluateXPath = rootNode.evaluateXPath("//div[@class='infobox1']/div[1]/ul[1]/li[1]");
        if(evaluateXPath.length>0){
            TagNode tagNode = (TagNode)evaluateXPath[0];
            System.out.println(tagNode.getText().toString());
        }
    }


    /**
     * 图片验证码查询
     * http://gsxt.zjaic.gov.cn/search/doGetAppSearchResult.do
     * clickType:1
     * verifyCode:瘦骨嶙峋
     * name:呵呵
     * @throws Exception
     */
//	@Test
    public void test2() throws Exception {
        HttpClientBuilder builder = HttpClients.custom();
        //设置浏览器信息
        builder.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36");
        //设置请求头信息
        Collection<BasicHeader> defaultHeaders = new ArrayList<BasicHeader>();
        defaultHeaders.add(new BasicHeader("Accept-Encoding", "gzip, deflate, sdch"));
        defaultHeaders.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8"));
        defaultHeaders.add(new BasicHeader("Connection", "keep-alive"));
        defaultHeaders.add(new BasicHeader("Host", "gsxt.zjaic.gov.cn"));
        defaultHeaders.add(new BasicHeader("Referer", "http://gsxt.zjaic.gov.cn/zhejiang.jsp"));
        builder.setDefaultHeaders(defaultHeaders);

        CloseableHttpClient client = builder.build();
        //获取验证码
        HttpGet httpGet = new HttpGet("http://gsxt.zjaic.gov.cn/common/captcha/doReadKaptcha.do");
        CloseableHttpResponse response = client.execute(httpGet);
        InputStream inputStream = response.getEntity().getContent();
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\c.jpeg");
        byte[] by = new byte[1024];
        int len =0;
        while((len = inputStream.read(by))!=-1){
            fileOutputStream.write(by, 0, len);
        }
        inputStream.close();
        fileOutputStream.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        System.out.println("请输入:");
        str = br.readLine();
        //执行查询
        HttpPost httpPost = new HttpPost("http://gsxt.zjaic.gov.cn/search/doGetAppSearchResult.do");
        List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("clickType", "1"));
        parameters.add(new BasicNameValuePair("verifyCode", str));
        parameters.add(new BasicNameValuePair("name", "呵呵"));
        //给中文参数设置编码
        HttpEntity entity = new UrlEncodedFormEntity(parameters,"utf-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse execute = client.execute(httpPost);
        System.out.println(EntityUtils.toString(execute.getEntity(),"utf-8"));
    }
}
