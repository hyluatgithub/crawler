package com.aadata.crawler.jbk;

import com.aadata.crawler.model.DiseaseDetail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 解析疾病详情
 */
public class DiseaseDetailThread implements Runnable {
    private static final String urlPrefix = "http://jib.xywy.com/il_sii";

    private List<String> diseaseIds;
    private List<DiseaseDetail> diseaseDetails;
    private CountDownLatch countDownLatch;
    // 并发症
    private Map<String, Map<String, String>> acompanyWithMap = new ConcurrentHashMap<>();

    public DiseaseDetailThread(List<String> diseaseIds, List<DiseaseDetail> diseaseDetails, CountDownLatch countDownLatch) {
        this.diseaseIds = diseaseIds;
        this.diseaseDetails = diseaseDetails;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        if (!CollectionUtils.isEmpty(diseaseIds)) {
            this.getDetails(diseaseIds);
        }
    }

    private synchronized void getDetails(List<String> ids) {
        List<DiseaseDetail> diseaseDetails = Collections.synchronizedList(new ArrayList<>());
        ids.forEach(e -> {
            System.out.println("=============开始解析疾病>>>" + e);
            DiseaseDetail diseaseDetail = new DiseaseDetail();
            diseaseDetail.setId(e);
//            getDiseaseDetail4About(diseaseDetail);
//            getDiseaseDetail4Yuanyin(diseaseDetail);
//            getDiseaseDetail4Yufang(diseaseDetail);
//            getDiseaseDetail4Food(diseaseDetail);
            getDiseaseDetail4Symptom(diseaseDetail);
            diseaseDetails.add(diseaseDetail);
            System.out.println("==============结束解析症状>>>" + e);
        });
        this.diseaseDetails.addAll(diseaseDetails);
        countDownLatch.countDown();
        System.out.println("当前计数器：：" + countDownLatch.getCount());
    }

    /**
     * 解析疾病详情-基础信息
     *
     * @param diseaseDetail
     */
    private DiseaseDetail getDiseaseDetail4About(DiseaseDetail diseaseDetail) {
        String id = diseaseDetail.getId();
        System.out.println("开始解析>>>基础信息" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/gaishu/").append(id).append(".htm");
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 疾病名称
        String name = document.select(".jb-name").text();

        // 提取内容
        Elements elements = document.select(".jib-articl");

        // 简介
        String desc = elements.select(".jib-articl-con>p").text();

        // 基础知识
        Elements select = elements.select(".articl-know").get(0).select(".txt-right");
        //疾病易感人群
        String easyGet = select.get(2).text();
        // TODO 并发症
        Elements acompanyWith = select.get(4).select("a");
        String href, title;
        String idTemp;
        Map<String, String> diseases = new ConcurrentHashMap<>();
        for (Element element : acompanyWith) {
            href = element.attr("href");
            title = element.text();
            idTemp = href.split("_")[href.split("_").length - 1].replace(".htm", "");
            System.out.println("并发症：：" + idTemp + "," + title);
            diseases.put(idTemp, title);
        }
        if (!acompanyWithMap.containsKey(id)) {
            acompanyWithMap.put(id, diseases);
        }

        // 治疗常识
        Elements select2 = elements.select(".articl-know").get(1).select(".txt-right");
        // 治疗方式
        String cureWay = select2.get(1).text();
        // 治疗周期
        String cureLastTime = select2.get(2).text();
        // 治愈率
        String cureProb = select2.get(3).text();

        diseaseDetail.setName(name);
        diseaseDetail.setDesc(desc);
        diseaseDetail.setEasyGet(easyGet);
        diseaseDetail.setCureWay(cureWay);
        diseaseDetail.setCureLastTime(cureLastTime);
        diseaseDetail.setCuredProb(cureProb);

        diseaseDetails.add(diseaseDetail);
        System.out.println("基础信息>>>" + id);
        return diseaseDetail;
    }


    /**
     * 解析疾病详情-病因
     *
     * @param diseaseDetail
     */
    private DiseaseDetail getDiseaseDetail4Yuanyin(DiseaseDetail diseaseDetail) {
        String id = diseaseDetail.getId();
        System.out.println("开始解析疾病>>>病因>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/cause/").append(id).append(".htm");
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select(".jib-articl >p");
        if (elements == null) {
            elements = document.select(".jib-articl>div");
        }
        diseaseDetail.setCause(elements == null ? "" : elements.text());
        System.out.println("结束解析疾病>>>病因>>>" + id);
        return diseaseDetail;
    }

    /**
     * 解析疾病详情-预防
     *
     * @param diseaseDetail
     */
    private DiseaseDetail getDiseaseDetail4Yufang(DiseaseDetail diseaseDetail) {
        String id = diseaseDetail.getId();
        System.out.println("开始解析>>>预防>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/prevent/").append(id).append(".htm");
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select(".jib-articl >p");
        if (elements.isEmpty()) {
            elements = document.select(".jib-articl >div");
        }
        diseaseDetail.setPrevent(elements == null ? "" : elements.text());
        System.out.println("结束解析>>>预防>>>" + id);
        return diseaseDetail;
    }


    /**
     * 解析疾病详情-食疗
     *
     * @param diseaseDetail
     */
    private DiseaseDetail getDiseaseDetail4Food(DiseaseDetail diseaseDetail) {
        String id = diseaseDetail.getId();
        System.out.println("开始解析>>>食疗>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/food/").append(id).append(".htm");
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String good = document.select(".diet-good-txt").eq(0).text();
        String goodFood = document.select(".diet-item").eq(1).select(".diet-opac-txt").text();
        diseaseDetail.setDietGood(good);
        diseaseDetail.setGoodFoods(goodFood);

        String bad = document.select(".diet-good-txt").eq(1).text();
        String badFood = document.select(".diet-item").eq(2).select(".diet-opac-txt").text();
        diseaseDetail.setDietBad(bad);
        diseaseDetail.setBadFoods(badFood);
        System.out.println("结束解析>>>食疗>>>" + id);
        return diseaseDetail;
    }

    /**
     * 解析疾病详情-症状
     *
     * @param diseaseDetail
     */
    private DiseaseDetail getDiseaseDetail4Symptom(DiseaseDetail diseaseDetail) {
        String id = diseaseDetail.getId();
        System.out.println("开始解析疾病>>>症状>>>" + id);
        StringBuilder url = new StringBuilder(urlPrefix).append("/symptom/").append(id).append(".htm");
        Document document = null;
        try {
            document = Jsoup.parse(new URL(url.toString()).openStream(), "GBK", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select(".jib-articl >span");
        String href;
        List<Long> symptoms = new ArrayList<>();
        Elements select = elements.select(".gre");
        for (Element element : select) {
            href = element.attr("href");
            id = href.replace("http://zzk.xywy.com/", "").replace("_gaishu.html", "");
            symptoms.add(Long.valueOf(id));
        }
        diseaseDetail.setSymptoms(symptoms);
        System.out.println("结束解析疾病>>>症状>>>" + id);
        return diseaseDetail;
    }

}
