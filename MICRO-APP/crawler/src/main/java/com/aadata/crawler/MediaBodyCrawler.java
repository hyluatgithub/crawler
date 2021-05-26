package com.aadata.crawler;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aadata.crawler.model.Symptom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * 部位与症状关系抓取
 */
public class MediaBodyCrawler {
    private static Map<String, String> bodyMap = new LinkedHashMap<>();
    private static List<String> bodyArr = Collections.synchronizedList(new ArrayList<>());
    private static Map<String, List<Symptom>> bodyZhengzhuang = new LinkedHashMap<>();
    private static final String urlPrefix = "http://zzk.xywy.com";

    public static void main(String[] args) {
        try {
            MediaBodyCrawler crawler = new MediaBodyCrawler();
            crawler.getDepartment("http://zzk.xywy.com/p/toubu.html");

            bodyMap.forEach((k, v) -> {
                System.out.println(k + "," + v);
            });
            bodyArr.forEach(e -> {
                try {
                    crawler.getBodyAndZhengzhuang(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            bodyZhengzhuang.forEach((k, v) -> {
                System.out.println(k + ":" + v);
            });

            crawler.createExcel(bodyZhengzhuang);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getBodyAndZhengzhuang(String url) throws IOException {
        String key = url;
        url = urlPrefix + url;
        Document document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
        // 情况一 有子部门的解析
        Elements select = document.select(".jblist-con-ill>.ks-ill-txt>.ks-ill-list>li>a");
        String href;
        String text;
        String id;
        Symptom symptom;
        List<Symptom> symptoms = new ArrayList<>();
        for (Element element : select) {
            System.out.println("症状 = " + element);
            href = element.attr("href");
            text = element.attr("title");
            id = href.split("_")[0].replace("/", "");
            symptom = new Symptom(id, text, href, 1);
            symptoms.add(symptom);
        }
        bodyZhengzhuang.put(key, symptoms);
    }

    /**
     * 解析页面数据，含有自己部门的，将子部门存入list
     *
     * @param url
     * @throws IOException
     */
    private void getDepartment(String url) throws IOException {
        Document document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
        // 情况一 有子部门的解析
        Elements select = document.select(".jbk-nav-list>li");
        String href;
        String text;
        for (Element element : select) {
            System.out.println("主部位 = " + element.selectFirst("a"));
            href = element.selectFirst("a").attr("href");
            text = element.selectFirst("a").text();
            System.out.println(href + "" + text);

            Elements select1 = element.select(".jbk-sed-menu>li>a");
            if (select1.isEmpty()) {
                bodyMap.put(href, text);
                bodyArr.add(href);
            } else {
                for (Element element3 : select1) {
                    System.out.println("子部位 = " + element3);
                    href = element3.attr("href");
                    text = element3.text();
                    System.out.println(href + "" + text);
                    bodyMap.put(href, text);
                    bodyArr.add(href);
                }
            }

        }


    }


    /**
     * 生成部位与症状excel
     *
     * @param bodyZhengzhuang
     */
    private void createExcel(Map<String, List<Symptom>> bodyZhengzhuang) {
        ArrayList<Map<String, Object>> rows = new ArrayList<>();

        bodyZhengzhuang.forEach((k, v) -> {
            // 部位
            String buwei = bodyMap.get(k);
            v.forEach(e -> {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("部位", buwei);
                row.put("部位链接", k);
                row.put("症状编码", e.getId());
                row.put("症状", e.getTitle());
                row.put("症状链接", e.getHref());
                rows.add(row);
            });
        });

//        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(row1, row2);
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("G:/buweiAndZhengzhuang.xlsx");
        // 合并单元格后的标题行，使用默认标题样式
//        writer.merge(rows.size() - 1, "一班成绩单");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(rows, true);
        // 关闭writer，释放内存
        writer.close();
    }

}
