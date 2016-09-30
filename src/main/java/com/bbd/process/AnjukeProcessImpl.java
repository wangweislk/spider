package com.bbd.process;

import com.bbd.domain.Page;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bbd on 2016/9/29.
 */
public class AnjukeProcessImpl implements Processalbe{
    Logger logger = LoggerFactory.getLogger(JDProcessImpl.class);

    public void process(Page page) {
        String content = page.getContent();
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(content);
        if(page.getUrl().startsWith("http://cd.fang.anjuke.com/loupan/all")){
            // 处理分页
            try {
                // 下一页
                String xpath = "//*[@class=\"next-page next-link\"]";
                String attrname = "href";
                String nextUrl = getAttrValueeByXPath(rootNode, xpath, attrname);
                if(nextUrl!=null || !nextUrl.contains("tiaofangjie")){
                    page.addUrl(nextUrl);
                }


                //获取当前页的所有商品  //*[@id="container"]/div[2]/div[1]/div[4]
                // //*[@id="container"]/div[2]/div[1]/div[4]/div[1]/a
                Object[] productXPath = rootNode.evaluateXPath("//*[@class=\"key-list\"]/div/a");
                if(productXPath != null && productXPath.length>0){
                    for(Object obj :productXPath){
                        TagNode aNode = (TagNode) obj;
                        String url = aNode.getAttributeByName("href");
                        if(url!=null || !nextUrl.contains("tiaofangjie")){
                            page.addUrl(url);
                        }
                    }
                }

            } catch (XPatherException e) {
                e.printStackTrace();
            }
        }else{
            // 解析商品
            parseProduct(page,rootNode);

        }

    }

    private String getAttrValueeByXPath(TagNode rootNode, String xpath, String attrname) throws XPatherException {
        String nextUrl = null;
        Object[] nextUrlXPath = rootNode.evaluateXPath(xpath);
        if(nextUrlXPath != null && nextUrlXPath.length>0){
            TagNode nexUrlNode = (TagNode) nextUrlXPath[0];
            nextUrl = nexUrlNode.getAttributeByName(attrname);
        }
        return nextUrl;
    }

    private void parseProduct(Page page, TagNode rootNode) {
        try {
            // title //*[@id="j-triggerlayer"]
            String title = getValueByXPath(rootNode,"//*[@id=\"j-triggerlayer\"]");
            page.addField("title",title);

            // status
            String status = getValueByXPath(rootNode,"//*[@id=\"header\"]/div[3]/div/div[2]/i");
            page.addField("status",status);

            // price
            String price = getValueByXPath(rootNode,"//*[@class=\"price\"]/p/em");
            page.addField("price",price);

            // address
            String address = getValueByXPath(rootNode,"//*[@class=\"lpAddr-text\"]");
            page.addField("address",address);

            // startdate
            String startdate = getValueByXPath(rootNode,"//*[@class=\"info-left\"]/li[1]/span");
            page.addField("startdate",startdate);

            // launchdate
            String launchdate = getValueByXPath(rootNode,"//*[@class=\"info-right\"]/li[1]/span");
            page.addField("launchdate",launchdate);

            // decorate_status
            String decorate_status = getValueByXPath(rootNode,"//*[@class=\"info-left\"]/li[2]/span");
            page.addField("decorate_status",decorate_status);


        } catch (XPatherException e) {
            e.printStackTrace();
        }


    }

    private String getValueByXPath(TagNode rootNode,String xpath) throws XPatherException {
        String value = null;
        Object[] titleEvaluateXPath  = rootNode.evaluateXPath(xpath);
        if (titleEvaluateXPath != null && titleEvaluateXPath.length > 0) {
            TagNode titleNode = (TagNode) titleEvaluateXPath[0];
            value = titleNode.getText().toString();
//            System.out.println(value);
        }
        return value;
    }
}
