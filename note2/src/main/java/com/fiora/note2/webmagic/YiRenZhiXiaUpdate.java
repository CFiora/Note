package com.fiora.note2.webmagic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class YiRenZhiXiaUpdate implements PageProcessor {
    public static final String charSet = "utf-8";
    public static final String URL = "https://ac.qq.com/Comic/ComicInfo/id/531490";
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
    @Autowired
    private EmailDataPipeline emailDataPipeline;

    @Override
    public void process(Page page) {
        String span = page.getHtml().css("ul.works-chapter-log span.ui-pl10").toString();
        String subSpan = span.replace("</span>","");
        String dateStr = subSpan.substring(subSpan.indexOf(">") + 1);
        String today = sf.format(new Date());
        log.info("today -> " + today);
        if(today.equals(dateStr)) {
            // log.info("today -> " + today);
            page.putField("update", "YiRenZhiXia");
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setCharset(charSet)
                .setTimeOut(10*1000)
                .setRetryTimes(3)
                .setRetrySleepTime(3000);
    }

    @Scheduled(cron = "0/30 * * ? * FRI")
    public void process() {
        Spider.create(new YiRenZhiXiaUpdate())
                .addUrl(URL)
                .addPipeline(this.emailDataPipeline)
                .run();
    }

    public static void main(String[] args) {
        Spider.create(new YiRenZhiXiaUpdate())
                .addUrl(URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))
                .run();
    }
}
