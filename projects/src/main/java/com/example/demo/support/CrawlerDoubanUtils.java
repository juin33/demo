package com.example.demo.support;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author kejun
 * @date 2019/1/4 上午10:49
 */
public class CrawlerDoubanUtils {

    /**
     * 根据书名爬取豆瓣书籍
     * @param bookName
     * @return
     */
    public static String getSearchUrl(String bookName){
        return "https://www.douban.com/search?cat=1001&q="+bookName;
    }

    /**
     * 根据书名获取搜索结果页dom
     * @param url  请求路径
     * @return
     */
    public static Document getDom(String url){
        try{
            //获取页面元素dom  其中.get()为发起get请求
            Document doc = Jsoup.connect(url).get();
            return doc;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取具体某个书籍的跳转路径
     * @param doc 页面dom
     * @param index 取书籍信息序号
     * @return
     */
    public static String getBookItemUrl(Document doc,int index){
        //根据class来获取元素  .get为获取第几个dom
        Element element = doc.getElementsByClass("result").get(index);
        //.first()为获取第一个dom
        Element element1 = element.getElementsByClass("title").first();
        //.getElementsByTag()为根据标签来获取dom
        Element element2 = element1.getElementsByTag("a").first();
        //.attr()获取标签中某个属性的值
        return element2.attr("href");
    }

    /**
     * 获得重定向后的豆瓣书籍地址
     * @param str
     * @return
     */
    public static String getUrl(String str){
        String realUrl = "";
        try {
            URL url = new URL(str);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.getResponseCode();
            realUrl=conn.getURL().toString();
            conn.disconnect();
        }catch (Exception e){
            e.printStackTrace();;
        }
        return realUrl;
    }
    /**
     * 获取图书封面图片
     * @param doc
     * @return
     */
    public static String getBookCoverImg(Document doc){
        String bookCoverImg = "";
        try {
            Element element = doc.getElementById("mainpic");
            Element element1 = element.getElementsByTag("a").first();
            Element element2 = element1.getElementsByTag("img").first();
            return element2.attr("src");
        }catch (Exception e){
            e.printStackTrace();
        }
        return bookCoverImg;
    }

    /**
     * 获取图书作者
     * @param doc
     * @return
     */
    public static String getBookAuhor(Document doc){
        String bookAuthor = "";
        try {
            Element element = doc.getElementById("info");
            Element element1 = element.getElementsByTag("a").first();
            //.text()为获取dom中文本信息
            return element1.text();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bookAuthor;
    }

    /**
     * 获取豆瓣评分
     * @param doc
     * @return
     */
    public static String getDoubanScore(Document doc){
        String doubanScore = "";
        try {
            Element element = doc.getElementsByClass("rating_num").first();
            return element.text();
        }catch (Exception e){
            e.printStackTrace();
        }
        return doubanScore;
    }

    /**
     * 获取书籍简介
     * @param doc
     * @return
     */
    public static String getBookIntroduction(Document doc){
        String articleIntroduction = "";
        try {
            Element element = doc.getElementsByClass("intro").first();
            return element.text();
        }catch (Exception e){
            e.printStackTrace();
        }
        return articleIntroduction;
    }

    public static String getBookDetailsUrl(String bookName){
        String url = getSearchUrl(bookName);
        Document doc = getDom(url);
        return getUrl(getBookItemUrl(doc,0));
    }

//    public static void main(String[] args){
//        String url = getBookDetailsUrl("明朝那些事");
//        Document doc = getDom(url);
//        System.out.println(getDoubanScore(doc));
//    }

//    public static void main(String[] args){
//        String url = getSearchUrl("明朝那些事");
//        Document doc = getDom(url);
//        System.out.println(getBookItemUrl(doc,0));
//    }

//    public static void main(String[] args){
//        String url = getSearchUrl("明朝那些事");
//        Document doc = getDom(url);
//        System.out.println(getUrl(getBookItemUrl(doc,0)));
//    }

}
