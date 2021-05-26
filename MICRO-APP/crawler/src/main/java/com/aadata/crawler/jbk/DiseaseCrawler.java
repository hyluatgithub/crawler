package com.aadata.crawler.jbk;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.aadata.crawler.MediaDepartmentCrawler;
import com.aadata.crawler.SymptomDetailThread;
import com.aadata.crawler.model.DiseaseDetail;
import com.aadata.crawler.model.SymptomDetail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 全部疾病
 */
public class DiseaseCrawler {
    private static final String urlPrefix = "http://jib.xywy.com/html/id.html";
    private volatile CountDownLatch countDownLatch = new CountDownLatch(26);
    private static List<String> symptomIdArr = new ArrayList<>();
    private static List<DiseaseDetail> symptomDetails = Collections.synchronizedList(new ArrayList<>());
    private static ExecutorService threadPool = Executors.newFixedThreadPool(80);

    public static void main(String[] args) throws IOException {
        // 计时
        TimeInterval timer = DateUtil.timer();
        DiseaseCrawler crawler = new DiseaseCrawler();
        String url;
        for (int i = 97; i <= 122; i++) {
            url = urlPrefix.replace("id", String.valueOf((char) i));
            crawler.getZhengzhuangList(url);
//            List<String> subIdArray = symptomIdArr.subList(start, end);
//            threadPool.execute(new SymptomDetailThread(subIdArray, symptomDetails, countDownLatch));
        }
        timer.intervalRestart();

        crawler.getDetail();
        timer.intervalSecond();
    }

    private void getZhengzhuangList(String url) throws IOException {
        Document document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
        // 情况一 有子部门的解析
        Elements select = document.select(".ks-zm-list>li>a");
        String href;
        String id;
        for (Element element : select) {
            href = element.attr("href");
//            id = href.split("_")[0].replace("/", "");
            id = href.split("_")[2].replace(".htm", "");
            symptomIdArr.add(id);
        }
    }

    private synchronized void getDetail() {
        int size = symptomIdArr.size();
        int step = 100;
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
            threadPool.execute(new DiseaseDetailThread(subIdArray, symptomDetails, countDownLatch));
            start += step;
        }
        try {
            countDownLatch.await();
            System.out.println("准备导出数据" + symptomDetails.size());
            Thread.sleep(2000);
            new JBKMediaDepartmentCrawler().createDetailsExcel(symptomDetails);
            threadPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
