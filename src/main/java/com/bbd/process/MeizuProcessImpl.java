package com.bbd.process;

import com.bbd.domain.Page;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * Created by bbd on 2016/9/22.
 */
public class MeizuProcessImpl implements Processalbe{

    public void process(Page page) {
        String content = page.getContent();
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(content);
        if(page.getUrl().startsWith("http://detail.meizu.com/item")){
            parseProduct(page,rootNode);
        }else{
            // 解析下一页
        }

    }

    private void parseProduct(Page page, TagNode rootNode) {
        try{
            // 标题
            Object[] titleXPath = rootNode.evaluateXPath("//*[@id=\"property\"]/div[1]/h1");
            if(titleXPath != null && titleXPath.length>0){
                TagNode titleNode = (TagNode) titleXPath[0];
                page.addField("title",titleNode.getText().toString());
            }

            // 价格 //*[@id="J_price"]
            Object[] priceXPath = rootNode.evaluateXPath("//*[@id=\"J_price\"]");
            if(priceXPath != null && priceXPath.length>0){
                TagNode priceNode = (TagNode) priceXPath[0];
                System.out.println(priceNode.getText()+"----");
                page.addField("price",priceNode.getText().toString());
            }

            // 图片URL //*[@id="J_imgBooth"]/img
            Object[] pictureEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"J_imgBooth\"]/img");
            if (pictureEvaluateXPath != null && pictureEvaluateXPath.length > 0) {
                TagNode pictureNode = (TagNode) pictureEvaluateXPath[0];
                String picurl = pictureNode.getAttributeByName("src");
                page.addField("pictureurl",picurl);
            }

            // 规格参数



        }catch (Exception e){

        }
    }
}
