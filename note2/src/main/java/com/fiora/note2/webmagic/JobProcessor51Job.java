package com.fiora.note2.webmagic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fiora.note2.model.JobInfo;
import com.fiora.note2.util.HttpUtils;
import com.fiora.note2.util.SalaryUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
/**
 * 51job.com java Beijing
 */
public class JobProcessor51Job implements PageProcessor {
    public static final String charSet = "gbk";
    public static final String marker = "@fiora";
    public static final String URL = "https://search.51job.com/list/010000,000000,0000,01%252c32,9,99,Java,2,"+marker+".html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";

    @Autowired
    private SpringDataPipeline springDataPipeline;

    @Override
    public void process(Page page) {
        String html = page.getHtml().toString();
        int indexOfResult = html.indexOf("window.__SEARCH_RESULT__");
        if (indexOfResult != -1) {
            //列表页
            String subHtml = html.substring(indexOfResult);
            String jsonStr = subHtml.substring(0, subHtml.indexOf("</script>")).replace("window.__SEARCH_RESULT__ = ", "");
            log.info(jsonStr);
            JSONObject json = JSONObject.parseObject(jsonStr);
            JSONArray auctionAds = json.getJSONArray("auction_ads");
            for (Object obj : auctionAds) {
                JSONObject jsonObject = (JSONObject) obj;
                String jobInfoUrl = jsonObject.getString("job_href");
                page.addTargetRequest(jobInfoUrl);
            }
            JSONArray engineResults = json.getJSONArray("engine_search_result");
            for (Object obj : engineResults) {
                JSONObject jsonObject = (JSONObject) obj;
                String jobInfoUrl = jsonObject.getString("job_href");
                page.addTargetRequest(jobInfoUrl);
            }
        } else {
            saveJobInfo(page);
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setCharset(charSet)
                .setTimeOut(10*1000)
                .setRetryTimes(3)
                .setRetrySleepTime(3000);
    }

//    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(initialDelay = 100L, fixedDelay = 1000000L)
    public void process() {
        int totalPage = getTotalPage();
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            urls.add(URL.replace(marker,i+""));
        }

        Spider.create(new JobProcessor51Job())
//                .addUrl(urls.get(0))
                .addUrl(urls.toArray(new String[urls.size()]))
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .addPipeline(this.springDataPipeline)
                .thread(10)
                .run();

    }

    private void saveJobInfo(Page page) {
        JobInfo jobInfo = new JobInfo();
        Html html = page.getHtml();
        String companyName = html.css("div.cn p.cname a", "text").get();
        jobInfo.setCompanyName(companyName);
        if (html.css("div.bmsg").nodes().size() > 1) {
            jobInfo.setCompanyAddr(Jsoup.parse(html.css("div.bmsg").nodes().get(1).toString()).text()
                    .replace("上班地址：","").replace(" 地图",""));
        }
        jobInfo.setCompanyInfo(Jsoup.parse(html.css("div.tmsg").toString()).text());
        jobInfo.setJobName(html.css("div.cn h1", "text").get());
        String jobAddr = html.css("div.cn p.msg", "text").get();
        jobInfo.setJobAddr(jobAddr);
        jobInfo.setJobInfo(Jsoup.parse(html.css("div.job_msg").toString()).text());
        String salaryInfo = html.css("div.cn strong", "text").get();
        if (!ObjectUtils.isEmpty(salaryInfo)) {
            Object[] result = SalaryUtil.getIntervalAndSalaryRange(salaryInfo);
            jobInfo.setSalaryUnit((String) result[0]);
            jobInfo.setSalaryMin(Integer.parseInt((String) result[1]));
            jobInfo.setSalaryMax(Integer.parseInt((String) result[2]));
        }
        jobInfo.setUrl(page.getUrl().get());
        String hasTime = jobAddr.split("发布")[0];
        String time = hasTime.substring(hasTime.length()-5);
        jobInfo.setTime(time);
        log.info("jobInfo -> " + jobInfo);
        page.putField("jobInfo", jobInfo);
    }

    private int getTotalPage() {
        String url = "https://search.51job.com/list/010000,000000,0000,01%252c32,9,99,Java,2,1.html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";
        String html = HttpUtils.httpGetRequestStr(url, null, null, "gbk");
        int indexOfResult = html.indexOf("window.__SEARCH_RESULT__");
        String subHtml = html.substring(indexOfResult);
        String jsonStr = subHtml.substring(0, subHtml.indexOf("</script>")).replace("window.__SEARCH_RESULT__ = ", "");
        System.out.println(jsonStr);
        JSONObject json = JSONObject.parseObject(jsonStr);
        int total = 118;
        try {
            total = Integer.parseInt(json.getString("total_page"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    public static void main(String[] args) {
        List<String> urls = new ArrayList<>();
        for (int i = 1; i <= 117; i++) {
            urls.add(URL.replace(marker,i+""));
        }
        Spider.create(new JobProcessor51Job())
//                .addUrl(urls.toArray(new String[urls.size()]))
                .addUrl(urls.get(0))
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(10)
                .run();
    }
}
