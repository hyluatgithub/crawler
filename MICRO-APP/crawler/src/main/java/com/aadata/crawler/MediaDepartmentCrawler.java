package com.aadata.crawler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aadata.crawler.model.Symptom;
import com.aadata.crawler.model.SymptomDetail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lhy13
 */
public class MediaDepartmentCrawler {
    private static final String urlPrefix = "http://zzk.xywy.com";
    // 存放全部
    private static Map<String, Map<String, String>> allDepartment = new LinkedHashMap<>(48);
    // 存放子级科室
    public static Map<String, String> linkedHashMap;
    // 存放症状
    private static Map<String, List<Symptom>> symptomMap = new LinkedHashMap<>();

    private static Map<String, String> departmentMap = new HashMap<>();
    private CountDownLatch countDownLatch;
    private static List<SymptomDetail> symptomDetails = Collections.synchronizedList(new ArrayList<>(7000));
    private static ExecutorService threadPool = Executors.newFixedThreadPool(50);
    private List<String> symptomIdArr = new ArrayList<>();

    public static void main(String[] args) {
        // 计时
        TimeInterval timer = DateUtil.timer();
        MediaDepartmentCrawler crawler = new MediaDepartmentCrawler();

        departmentMap.put("/p/neike.html", "内科");
        departmentMap.put("/p/waike.html", "外科");
        departmentMap.put("/p/fuchanke.html", "妇产科");
        departmentMap.put("/p/chuanranke.html", "传染科");
        departmentMap.put("/p/shengzhijiankang.html", "生殖健康");
        departmentMap.put("/p/nanke.html", "男科");
        departmentMap.put("/p/pifuxingbingke.html", "皮肤性病科");
        departmentMap.put("/p/zhongyike.html", "中医科");
        departmentMap.put("/p/zhongxiyijieheke.html", "中西医结合科");
        departmentMap.put("/p/wuguanke.html", "五官科");
        departmentMap.put("/p/jingshenke.html", "精神科");
        departmentMap.put("/p/xinlike.html", "心理科");
        departmentMap.put("/p/erke.html", "儿科");
        departmentMap.put("/p/yingyangke.html", "营养科");
        departmentMap.put("/p/zhongliuke.html", "肿瘤科");
        departmentMap.put("/p/qitakeshi.html", "其他科室");
        departmentMap.put("/p/jizhenke.html", "急诊科");
        departmentMap.put("/p/tijianke.html", "体检科");

        String[] rootDepart = new String[]{
                "/p/neike.html",
                "/p/waike.html",
                "/p/fuchanke.html",
//                "/p/chuanranke.html",
                "/p/shengzhijiankang.html",
//                "/p/nanke.html",
                "/p/pifuxingbingke.html",
                "/p/zhongyike.html",
//                "/p/zhongxiyijieheke.html",
                "/p/wuguanke.html",
//                "/p/jingshenke.html",
//                "/p/xinlike.html",
                "/p/erke.html",
//                "/p/yingyangke.html",
                "/p/zhongliuke.html",
                "/p/qitakeshi.html"
//                "/p/jizhenke.html",
//                "/p/tijianke.html"
        };

        // 初始爬取的源种子，无子科室
        String[] rootDepart2 = new String[]{
                "/p/chuanranke.html",
                "/p/nanke.html",
                "/p/zhongxiyijieheke.html",
                "/p/jingshenke.html",
                "/p/xinlike.html",
                "/p/yingyangke.html",
                "/p/jizhenke.html",
                "/p/tijianke.html"
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
                    crawler.getSymptom(k1);
                });
//                System.out.println("ooooooooooooooooooooooo");
            });
//            System.out.println("====有子科室的数据读取完毕======");

//            System.out.println("====读取主科室对应症状======");
            for (String url : rootDepart2) {
                crawler.getSymptom(url);
//                System.out.println("xxxxxxxxxxxxxxxxxxx");
            }
            System.out.println("====读取主科室对应症状完毕======");

            // 科室爬取耗时
            System.out.println("读取科室信息耗时++++++++++" + timer.interval());

//            System.out.println("====导出症状======");
//            crawler.createExcel(symptomMap);
//            System.out.println("====导出症状症状完毕======");

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
            threadPool.execute(new SymptomDetailThread(subIdArray, symptomDetails, countDownLatch));
            start += step;
        }
        try {
            countDownLatch.await();
            System.out.println("准备导出数据" + symptomDetails.size());
            Thread.sleep(2000);
            createDetailsExcel(symptomDetails);
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
        Elements select = document.select(".jblist-con");

        linkedHashMap = new LinkedHashMap<>();
        // 有子部门，解析子部门数据
        Elements elements = select.select(".gre");
        String href;
        String text;
        for (Element element : elements) {
            href = element.attr("href");
            text = element.text();
//            System.out.println(href + "" + text);
            linkedHashMap.put(href, text);
            departmentMap.put(href, text);
        }
        allDepartment.put(key, linkedHashMap);

    }

    /**
     * 解析症状
     *
     * @param url 有子科室传子科室，无子科室传本体
     */
    private void getSymptom(String url) {
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
            title = element.attr("title");
            id = href.split("_")[0].replace("/", "");
//            System.out.println(id + " " + href + " " + title);
            symptom = new Symptom(id, title, href, 1);
            symbols.add(symptom);
            symptomIdArr.add(id);
        }
        symptomMap.put(key, symbols);
    }


    /**
     * 生成科室与症状excel
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
        ExcelWriter writer = ExcelUtil.getWriter("G:/keshiAndZhengzhuang.xlsx");
        // 合并单元格后的标题行，使用默认标题样式
//        writer.merge(rows.size() - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

    /**
     * 生成症状详情excel
     *
     * @param symptomDetails
     */
    public void createDetailsExcel(List<SymptomDetail> symptomDetails) {
        ArrayList<Map<String, Object>> rows = new ArrayList<>();
        symptomDetails.stream().forEach(e -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("症状编码", e.getId());
            if (e.getJieshao().length() > 5000) {
                row.put("介绍", e.getJieshao().substring(0, 5000));
                System.out.println("介绍 长度超长==============");
            } else {
                row.put("介绍", e.getJieshao());
            }
            row.put("介绍关键字", e.getJieshaoKeyword());
//            row.put("病因", e.yuanyin);
//            if (e.getYuanyin().length() > 5000) {
//                row.put("病因", e.getYuanyin().substring(0, 5000));
//                System.out.println("病因 长度超长==============");
//            } else {
//                row.put("病因", e.getYuanyin());
//            }
//            row.put("病因关键字", e.getYuanyinKeyword());
//            row.put("预防", e.yufang);
            if (e.getYufang().length() > 5000) {
                row.put("预防", e.getYufang().substring(0, 5000));
                System.out.println("预防 长度超长==============");
            } else {
                row.put("预防", e.getYufang());
            }
//            row.put("检查", e.jiancha);
            if (e.getJiancha().length() > 5000) {
                row.put("检查", e.getJiancha().substring(0, 5000));
                System.out.println("检查 长度超长==============");
            } else {
                row.put("检查", e.getJiancha());
            }
            row.put("检查关键字", e.getJianchaKeyword());

//            if (e.getZhenduan().length() > 5000) {
//                row.put("鉴别诊断", e.getZhenduan().substring(0, 5000));
//                System.out.println("鉴别诊断 长度超长==============");
//            } else {
//                row.put("鉴别诊断", e.getZhenduan());
//            }
//            row.put("鉴别诊断", e.zhenduan);
//            row.put("鉴别诊断关键字", e.getZhenduanKeyword());
            row.put("相关症状", e.getAboutSymptom());
            row.put("宜吃", e.getGoodFood());
            row.put("宜吃食物", e.getGoodFoods());
            row.put("忌吃", e.getBadFood());
            row.put("忌吃食物", e.getBadFoods());
            rows.add(row);
        });

//        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("G:/zhengzhaung.xlsx");
        // 合并单元格后的标题行，使用默认标题样式
//        writer.merge(rows.size() - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

}
