package com.bbd.process;

import com.bbd.domain.Page;
import com.bbd.utils.PageUtils;
import com.sun.glass.ui.SystemClipboard;
import com.sun.javadoc.Tag;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析京东手机业务数据
 * Created by bbd on 2016/9/20.
 */
public class JDProcessImpl implements Processalbe{

    Logger logger = LoggerFactory.getLogger(JDProcessImpl.class);

    public void process(Page page) {
        String content = page.getContent();
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode rootNode = htmlCleaner.clean(content);
        if(page.getUrl().startsWith("http://item.jd.com/")){
            parseProduct(page,rootNode);
        }else{
            //解析列表页面
            //获取下一面的URL地址
            try {
                Object[] nextUrlXPath = rootNode.evaluateXPath("//*[@id=\"J_topPage\"]/a[2]");
                if(nextUrlXPath != null && nextUrlXPath.length>0){
                    TagNode nexUrlNode = (TagNode) nextUrlXPath[0];
                    String nextUrl = "http://list.jd.com"+nexUrlNode.getAttributeByName("href");
                    if(!nextUrl.equals("javascript:;")){ // 排除最后一页
                        page.addUrl(nextUrl);
                    }
                }

                //获取当前页的所有商品
                Object[] productXPath = rootNode.evaluateXPath("//*[@id=\"plist\"]/ul/li/div/div[1]/a");
                if(productXPath != null && productXPath.length>0){
                    for(Object obj :productXPath){
                        TagNode aNode = (TagNode)obj;
                        String url = "http:"+aNode.getAttributeByName("href");
//                        System.out.println(url);
                        page.addUrl(url);
                    }
                }

            } catch (XPatherException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 解析商品详细信息
     * @param page
     */
    private void parseProduct(Page page,TagNode rootNode) {
        try {
            // 标题
            Object[] titleEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"spec-img\"]");
            if (titleEvaluateXPath != null && titleEvaluateXPath.length > 0) {
                TagNode titleNode = (TagNode) titleEvaluateXPath[0];
                String title = titleNode.getAttributeByName("alt");
                page.addField("title",title);
            }
            // //*[@id="spec-img"]

            //图片地址
            Object[] pictureEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"spec-img\"]");
            if (pictureEvaluateXPath != null && pictureEvaluateXPath.length > 0) {
                TagNode pictureNode = (TagNode) pictureEvaluateXPath[0];
                String picurl = pictureNode.getAttributeByName("data-origin");
                page.addField("pictureurl","http:"+picurl);
            }

            // 价格
            String url = page.getUrl();
            Pattern pattern = Pattern.compile("http://item.jd.com/([0-9]+).html");
            Matcher matcher = pattern.matcher(url);
            String goodsId = "";
            if (matcher.find()) {
                goodsId = matcher.group(1);
            }
            page.setGoodsId(goodsId+"_jd");
            // http://p.3.cn/prices/mgets?skuIds=J_1856585
            String price_json = PageUtils.getContent("http://p.3.cn/prices/mgets?skuIds=J_" + goodsId);
            if(price_json.startsWith("[")){
                JSONArray jsonArray = new JSONArray(price_json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                page.addField("price",jsonObject.getString("p"));
            }else{
                logger.error("获取JSON参数错误,{}",price_json);
            }



            // 参数配置 //*[@id="detail"]/div[2]/div[2]/div[2]
            JSONArray guigeArray = new JSONArray();
            Object[] guigeEvaluateXPath = rootNode.evaluateXPath("//*[@id=\"detail\"]/div[2]/div[2]/div[2]");
            if (guigeEvaluateXPath != null && guigeEvaluateXPath.length > 0) {
                TagNode guige = (TagNode) guigeEvaluateXPath[0];
                List<TagNode> childTagList = guige.getChildTagList();
                if (childTagList.size() > 0) {
                    for (TagNode childTag : childTagList) {
                        List<TagNode> childTagList1 = childTag.getChildTagList();
                        if (childTagList1.size() == 2) {
                            JSONObject jsonObj = new JSONObject();

                            List<TagNode> dtTagNodes = childTagList1.get(1).getChildTagList();
                            if (dtTagNodes.size() > 0) {
                                JSONArray dtArr = new JSONArray();
                                for (int i = 0; i < dtTagNodes.size(); i += 2) {
                                    JSONObject obj = new JSONObject();
                                    obj.put(dtTagNodes.get(i).getText().toString(), dtTagNodes.get(i + 1).getText().toString());
                                    dtArr.put(obj);
                                }
                                jsonObj.put(childTagList1.get(0).getText().toString(), dtArr);
                            }
                            guigeArray.put(jsonObj);
                        }
                    }
                }
            }
//            System.out.println(guigeArray.toString());
            page.addField("spec",guigeArray.toString());


        } catch (XPatherException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
            logger.error("JSON解析错误");
        }
    }
}
