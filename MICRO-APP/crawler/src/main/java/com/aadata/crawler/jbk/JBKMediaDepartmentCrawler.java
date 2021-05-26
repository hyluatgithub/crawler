package com.aadata.crawler.jbk;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aadata.crawler.model.DiseaseDetail;
import com.aadata.crawler.model.Symptom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lhy13
 */
public class JBKMediaDepartmentCrawler {
    private static final String urlPrefix = "http://jib.xywy.com";
    // 存放全部
    private static Map<String, Map<String, String>> allDepartment = new LinkedHashMap<>();
    // 存放子级科室
    public static Map<String, String> linkedHashMap;
    // 存放疾病
    private static Map<String, List<Symptom>> symptomMap = new LinkedHashMap<>();

    private static Map<String, String> departmentMap = new LinkedHashMap<>();
    private CountDownLatch countDownLatch;
    private List<DiseaseDetail> diseaseDetails = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService threadPool = Executors.newFixedThreadPool(30);
    private List<String> symptomIdArr = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        // 计时
        TimeInterval timer = DateUtil.timer();
        JBKMediaDepartmentCrawler crawler = new JBKMediaDepartmentCrawler();
        departmentMap.put("/html/neike.html", "内科");
        departmentMap.put("/html/waike.html", "外科");
        departmentMap.put("/html/fuchanke.html", "妇产科");
        departmentMap.put("/html/chuanranke.html", "传染科");
        departmentMap.put("/html/shengzhijiankang.html", "生殖健康");
        departmentMap.put("/html/nanke.html", "男科");
        departmentMap.put("/html/pifuxingbingke.html", "皮肤性病科");
        departmentMap.put("/html/zhongyike.html", "中医科");
        departmentMap.put("/html/zhongxiyijieheke.html", "中西医结合科");
        departmentMap.put("/html/wuguanke.html", "五官科");
        departmentMap.put("/html/jingshenke.html", "精神科");
        departmentMap.put("/html/xinlike.html", "心理科");
        departmentMap.put("/html/erke.html", "儿科");
        departmentMap.put("/html/yingyangke.html", "营养科");
        departmentMap.put("/html/zhongliuke.html", "肿瘤科");
        departmentMap.put("/html/qitakeshi.html", "其他科室");
        departmentMap.put("/html/jizhenke.html", "急诊科");
        departmentMap.put("/html/ganbing.html", "肝病");

        String[] rootDepart = new String[]{
                "/html/neike.html",
                "/html/waike.html",
                "/html/fuchanke.html",
                "/html/shengzhijiankang.html",
                "/html/pifuxingbingke.html",
                "/html/zhongyike.html",
                "/html/wuguanke.html",
                "/html/erke.html",
                "/html/zhongliuke.html",
                "/html/qitakeshi.html"
        };

        // 初始爬取的源种子，无子科室
        String[] rootDepart2 = new String[]{
                "/html/chuanranke.html",
                "/html/nanke.html",
                "/html/zhongxiyijieheke.html",
                "/html/jingshenke.html",
                "/html/xinlike.html",
                "/html/yingyangke.html",
                "/html/jizhenke.html",
                "/html/ganbing.html"
        };

        try {
            // 读取科室
            for (String url : rootDepart) {
                crawler.getDepartment(url);
            }
            System.out.println("读取的子科室信息");
            allDepartment.forEach((k, v) -> {
//                System.out.println("主科室： " + k);
                v.forEach((k1, v1) -> {
//                    System.out.println("子科室：" + v1 + ":" + k1);
                    crawler.getDisease(k1);
                });
//                System.out.println("ooooooooooooooooooooooo");
            });
//            System.out.println("====有子科室的数据读取完毕======");

//            System.out.println("====读取主科室对应症状======");
            for (String url : rootDepart2) {
                crawler.getDisease(url);
//                System.out.println("xxxxxxxxxxxxxxxxxxx");
            }
            System.out.println("====读取主科室对应症状完毕======");

            // 科室爬取耗时
            System.out.println("读取科室信息耗时++++++++++" + timer.interval());

            System.out.println("====导出症状======");
//            crawler.createExcel(symptomMap);
            System.out.println("====导出症状症状完毕======");

            //  展示遍历的症状关系

            crawler.getDetail();
            // 科室爬取耗时
            System.out.println("科室爬取耗时++++++++++" + timer.interval());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private synchronized void getDetail() {
//        symptomIdArr.clear();
//        symptomIdArr.add("6250");
//        symptomIdArr.add("928");
        int size = symptomIdArr.size();
        int step = 200;
        int start = 0;
        int end = 0;
        int page = size / step + 1;
        countDownLatch = new CountDownLatch(page);
        while (start < size) {
            end = start + step;
            if (end > size) {
                end = size;
            }
            List<String> subIdArray = symptomIdArr.subList(start, end);
            threadPool.execute(new DiseaseDetailThread(subIdArray, diseaseDetails, countDownLatch));
            start += step;
        }
        try {
            countDownLatch.await();
            System.out.println("准备导出数据" + diseaseDetails.size());
            Thread.sleep(2000);
            createDetailsExcel(diseaseDetails);
            threadPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析页面数据，含有自己部门的，将子部门存入list
     *
     * @param url
     * @throws IOException
     */
    private void getDepartment(String url) throws IOException {
        String key = url;
        url = urlPrefix + url;
//        Document document = Jsoup.connect(url).get();
        Document document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
        // 情况一 有子部门的解析
        Elements select = document.select(".jblist-con>.ks-ill-box");

        linkedHashMap = new LinkedHashMap<>();
        // 有子部门，解析子部门数据
//        Elements elements = select.select(".gre");
        String href;
        String text;
        for (int i = 1; i < select.size(); i++) {
            href = select.get(i).select("a").attr("href");
            text = select.get(i).select(".fb").text().replace("常见病","");
//            System.out.println(href + "" + text);
            linkedHashMap.put(href, text);
            departmentMap.put(href, text);
        }
//        for (Element element : elements) {
//            href = element.attr("href");
//            text = element.text();
////            System.out.println(href + "" + text);
//            linkedHashMap.put(href, text);
//            departmentMap.put(href, text);
//        }
        allDepartment.put(key, linkedHashMap);

    }

    /**
     * 解析疾病
     *
     * @param url 有子科室传子科室，无子科室传本体
     */
    private void getDisease(String url) {
        String key = url;
        url = urlPrefix + url;
        Document document = null;
        try {
//            document = Jsoup.connect(url).get();
            document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select(".ks-ill-txt >.ks-ill-list >li >a");

        Symptom symptom;
        String id, href, title;
        List<Symptom> symbols = new ArrayList<>();
        for (Element element : elements) {
            href = element.attr("href");
            title = element.text();
            id = href.split("_")[2].replace(".htm", "");
//            System.out.println(id + " " + href + " " + title);
            symptom = new Symptom(id, title, href, 1);
            symbols.add(symptom);
            symptomIdArr.add(id);
        }
        symptomMap.put(key, symbols);
    }


    /**
     * 生成科室与疾病excel
     *
     * @param symptomMap
     */
    private void createExcel(Map<String, List<Symptom>> symptomMap) {
        ArrayList<Map<String, Object>> rows = new ArrayList<>();

        symptomMap.forEach((k, v) -> {
            // 科室
            String keshi = departmentMap.get(k);
            v.forEach(e -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("科室", keshi);
                row.put("科室链接", k);
                row.put("症状编码", e.getId());
                row.put("症状", e.getTitle());
                row.put("症状链接", e.getHref());
                rows.add(row);
            });
        });

//        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("G:/keshiAndJB.xlsx");
        // 合并单元格后的标题行，使用默认标题样式
//        writer.merge(rows.size() - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

    /**
     * 生成疾病详情excel
     *
     * @param diseaseDetails
     */
    public void createDetailsExcel(List<DiseaseDetail> diseaseDetails) {
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        diseaseDetails.stream().forEach(e -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("疾病编码", e.getId());
//            row.put("名称", e.getName());
//            if (e.getDesc().length() > 5000) {
//                row.put("简介", e.getDesc().substring(0, 5000));
//                System.out.println("介绍 长度超长==============");
//            } else {
//                row.put("简介", e.getDesc());
//            }
//            if (e.getCause().length() > 5000) {
//                row.put("病因", e.getCause().substring(0, 5000));
//                System.out.println("病因 长度超长==============");
//            } else {
//                row.put("病因", e.getCause());
//            }
//            if (e.getPrevent().length() > 5000) {
//                row.put("预防", e.getPrevent().substring(0, 5000));
//                System.out.println("预防 长度超长==============");
//            } else {
//                row.put("预防", e.getPrevent());
//            }
//            if (e.getCureWay().length() > 5000) {
//                row.put("治疗方式", e.getCureWay().substring(0, 5000));
//                System.out.println("治疗方式 长度超长==============");
//            } else {
//                row.put("治疗方式", e.getCureWay());
//            }
//            row.put("治疗周期", e.getCureLastTime());
//            row.put("治愈概率", e.getCuredProb());
//            row.put("疾病易感人群", e.getEasyGet());
//            row.put("宜吃", e.getDietGood());
//            row.put("宜吃食物", e.getGoodFoods());
//            row.put("忌吃", e.getDietBad());
//            row.put("忌吃食物", e.getBadFoods());
            if(e.getSymptoms()!=null){
                row.put("症状", StrUtil.join(",",e.getSymptoms()));
                rows.add(row);
            }

        });

        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("G:/jibing.xlsx");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

}
