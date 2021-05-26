package com.aadata.crawler;

import com.aadata.crawler.model.Symptom;
import com.aadata.crawler.model.SymptomDetail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 解析症状详情
 */
public class SymptomDetailThread implements Runnable {
    private static final String urlPrefix = "http://zzk.xywy.com";

    private List<String> symptomIds;
    private List<SymptomDetail> symptomDetails;
    private CountDownLatch countDownLatch;

    public SymptomDetailThread(List<String> symptomIds, List<SymptomDetail> symptomDetails, CountDownLatch countDownLatch) {
        this.symptomIds = symptomIds;
        this.symptomDetails = symptomDetails;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        if (!CollectionUtils.isEmpty(symptomIds)) {
            this.getDetails(symptomIds);
        }
    }

    private synchronized void getDetails(List<String> ids) {
        List<SymptomDetail> subSymptomDetails = Collections.synchronizedList(new ArrayList<>());
        ids.forEach(e -> {
            System.out.println("=============开始解析症状>>>" + e);
            SymptomDetail symptomDetail = new SymptomDetail();
            symptomDetail.setId(e);
            getSymptomDetail4About(symptomDetail);
            getSymptomDetail4Jieshao(symptomDetail);
//                getSymptomDetail4Yuanyin(symptomDetail);
            getSymptomDetail4Yufang(symptomDetail);
            getSymptomDetail4Jiancha(symptomDetail);
//                getSymptomDetail4Zhenduan(symptomDetail);
            getSymptomDetail4Food(symptomDetail);
            subSymptomDetails.add(symptomDetail);
            System.out.println("==============结束解析症状>>>" + e);
        });
        countDownLatch.countDown();
        symptomDetails.addAll(subSymptomDetails);
    }

    /**
     * 解析症状详情-相关症状
     *
     * @param symptomDetail
     */
    private SymptomDetail getSymptomDetail4About(SymptomDetail symptomDetail) {
        String id = symptomDetail.getId();
        System.out.println("开始解析>>>相关症状" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/").append(id).append("_jieshao.html");
        Document document = null;
        try {
//            document = Jsoup.connect(url.toString()).get();
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 提取介绍内容
        Elements elements = document.select(".about-zzlist >li");

        // 提取关键字
        Elements aboutElements = elements.select("a");
        List<Symptom> keywordArr = new ArrayList<>();
        Symptom symptom;
        String idTemp;
        String href,title;
        for (Element aboutElement : aboutElements) {
            href = aboutElement.attr("href");
            title = aboutElement.text();
            idTemp = href.split("_")[0].replace("/", "");
            symptom = new Symptom(idTemp, title, href, 1);
//            System.out.println("相关症状：：：" + href + " " + title + " " + 1);
            keywordArr.add(symptom);
        }
        // 提取内容文本
        symptomDetail.setAboutSymptom(keywordArr);
        System.out.println("结束解析症状>>>相关症状" + id);
        return symptomDetail;
    }

    /**
     * 解析症状详情-介绍
     *
     * @param symptomDetail
     */
    private SymptomDetail getSymptomDetail4Jieshao(SymptomDetail symptomDetail) {
        String id = symptomDetail.getId();
        System.out.println("开始解析症状>>>介绍" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/").append(id).append("_jieshao.html");
        Document document = null;
        try {
//            document = Jsoup.connect(url.toString()).get();
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 提取介绍内容
        Elements elements = document.select(".zz-articl >p");
        if(elements.isEmpty()){
            elements = document.select("zz-articl>div");
        }
        // 提取关键字
        Elements aboutElements = elements.select("a");
        List<Symptom> jieshaoKeyword = new ArrayList<>();
        Symptom symptom;
        String href, title;
        int type;
        for (Element aboutElement : aboutElements) {
            href = aboutElement.attr("href");
            title = aboutElement.text();
            type = getType(href);
            symptom = new Symptom(id, title, href, type);
//            System.out.println(href + " " + title + " " + type);
            jieshaoKeyword.add(symptom);
        }
        // 提取内容文本
        String text = elements.text();
//        System.out.println("疾病介绍：text = " + text);
        symptomDetail.setJieshao(text);
        if (!jieshaoKeyword.isEmpty()) {
            symptomDetail.setJieshaoKeyword(jieshaoKeyword);
        }
        System.out.println("结束解析症状>>>介绍" + id);
        return symptomDetail;
    }

    /**
     * 解析症状详情-病因
     *
     * @param symptomDetail
     */
    private SymptomDetail getSymptomDetail4Yuanyin(SymptomDetail symptomDetail) {
        String id = symptomDetail.getId();
        System.out.println("开始解析症状>>>病因>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/").append(id).append("_yuanyin.html");
        Document document = null;
        try {
//            document = Jsoup.connect(url.toString()).get();
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select(".zz-articl >p");
        StringBuilder yuanyin = null;
        for (int i = 1; i < elements.size(); i++) {
            if (yuanyin == null) {
                yuanyin = new StringBuilder(elements.text());
            } else {
                yuanyin.append("|").append(elements.text());
            }

        }
        // 提取关键字
        Elements aboutElements = elements.select("a");
        List<Symptom> keywordsArr = new ArrayList<>();
        Symptom symptom;
        String href, title;
        int type;
        for (Element aboutElement : aboutElements) {
            href = aboutElement.attr("href");
            title = aboutElement.text();
            type = getType(href);
            symptom = new Symptom(id, title, href, type);
//            System.out.println("病因：：：" + href + " " + title + " " + type);
            keywordsArr.add(symptom);
        }
        // 提取内容文本
//        String text = elements.text();
//        System.out.println("病因：text = " + text);
        symptomDetail.setYuanyin(yuanyin == null ? "" : yuanyin.toString());
        if (!keywordsArr.isEmpty()) {
            symptomDetail.setYuanyinKeyword(keywordsArr);
        }
        System.out.println("结束解析症状>>>病因>>>" + id);
        return symptomDetail;
    }

    /**
     * 解析症状详情-预防
     *
     * @param symptomDetail
     */
    private SymptomDetail getSymptomDetail4Yufang(SymptomDetail symptomDetail) {
        String id = symptomDetail.getId();
        System.out.println("开始解析>>>预防>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/").append(id).append("_yufang.html");
        Document document = null;
        try {
//            document = Jsoup.connect(url.toString()).get();
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select(".zz-articl >p");
        if(elements.isEmpty()){
            elements = document.select(".zz-articl >div");
        }
        // 提取内容文本
        String text = elements.text();
//        System.out.println("预防:text = " + text);
        symptomDetail.setYufang(text);
        System.out.println("结束解析>>>预防>>>" + id);
        return symptomDetail;
    }

    /**
     * 解析症状详情-检查
     *
     * @param symptomDetail
     */
    private SymptomDetail getSymptomDetail4Jiancha(SymptomDetail symptomDetail) {
        String id = symptomDetail.getId();
        System.out.println("开始解析>>>检查>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/").append(id).append("_jiancha.html");
        Document document = null;
        try {
//            document = Jsoup.connect(url.toString()).get();
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select(".zz-articl >p").eq(1);
        // 提取关键字
        Elements aboutElements = elements.select("a");
        List<Symptom> keywordArr = new ArrayList<>();
        Symptom symptom;
        String href, title;
        int type;
        for (Element aboutElement : aboutElements) {
            href = aboutElement.attr("href");
            title = aboutElement.text();
            type = getType(href);
            symptom = new Symptom(id, title, href, type);
//            System.out.println("检查:::" + href + " " + title + " " + type);
            keywordArr.add(symptom);
        }
        // 提取内容文本
        String text = elements.text();
//        System.out.println("检查:text = " + text);
        symptomDetail.setJiancha(text);
        if (!keywordArr.isEmpty()) {
            symptomDetail.setJianchaKeyword(keywordArr);
        }
        System.out.println("结束解析>>>检查>>>" + id);
        return symptomDetail;
    }

    /**
     * 解析症状详情-鉴别诊断
     *
     * @param symptomDetail
     */
    private SymptomDetail getSymptomDetail4Zhenduan(SymptomDetail symptomDetail) {
        String id = symptomDetail.getId();
        System.out.println("开始解析>>>鉴别诊断>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/").append(id).append("_zhenduan.html");
        Document document = null;
        try {
//            document = Jsoup.connect(url.toString()).get();
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select(".zz-articl >p");
        StringBuilder yuanyin = null;
        for (int i = 1; i < elements.size(); i++) {
            if (yuanyin == null) {
                yuanyin = new StringBuilder(elements.text());
            } else {
                yuanyin.append("|").append(elements.text());
            }

        }
        // 提取关键字
        List<Symptom> keywordsArr = new ArrayList<>();
        Symptom symptom;

        Elements aboutElements = elements.select("a");
        String href, title;
        int type;
        for (Element aboutElement : aboutElements) {
            href = aboutElement.attr("href");
            title = aboutElement.text();
            type = getType(href);
            symptom = new Symptom(id, title, href, type);
//            System.out.println("鉴别诊断:::" + href + " " + title + " " + type);
            keywordsArr.add(symptom);
        }
        // 提取内容文本
        String text = elements.text();
//        System.out.println("鉴别诊断:text = " + text);
        symptomDetail.setZhenduan(yuanyin == null ? "" : yuanyin.toString());
        if (!keywordsArr.isEmpty()) {
            symptomDetail.setZhenduanKeyword(keywordsArr);
        }
        System.out.println("结束解析>>>鉴别诊断>>>" + id);
        return symptomDetail;
    }

    /**
     * 解析症状详情-食疗
     *
     * @param symptomDetail
     */
    private SymptomDetail getSymptomDetail4Food(SymptomDetail symptomDetail) {
        String id = symptomDetail.getId();
        System.out.println("开始解析>>>食疗>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/").append(id).append("_food.html");
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String good = document.select(".diet-good-txt").eq(0).text();
        String goodFood = document.select(".diet-item").eq(0).select(".diet-opac-txt").text();
        symptomDetail.setGoodFood(good);
        symptomDetail.setGoodFoods(goodFood);

        String bad = document.select(".diet-good-txt").eq(1).text();
        String badFood = document.select(".diet-item").eq(1).select(".diet-opac-txt").text();
        symptomDetail.setBadFood(bad);
        symptomDetail.setBadFoods(badFood);
        System.out.println("结束解析>>>食疗>>>" + id);
        return symptomDetail;
    }

    /**
     * 获取关键字类型 1 症状  2 疾病  3 检查
     * 无匹配返回0 出错返回-1
     *
     * @param url
     * @return
     */
    private int getType(String url) {
        if (StringUtils.isEmpty(url)) {
            return -1;
        }
        if (url.contains("zzk")) {
            return 1;
        } else if (url.contains("jib")) {
            return 2;
        } else if (url.contains("jck")) {
            return 3;
        }
        return 0;
    }
}
