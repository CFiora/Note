package com.fiora.note2.webmagic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class HaiZeiWangUpdate implements PageProcessor {
    public static final String charSet = "utf-8";
    public static final String URL = "https://www.iqiyi.com/a_19rrhb3xvl.html";
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private EmailDataPipeline emailDataPipeline;

    @Override
    public void process(Page page) {
        String span = page.getHtml().css("span.title-update-progress a i.title-update-num").get();
        String subSpan = span.replace("</i>","");
        String dateStr = subSpan.substring(subSpan.indexOf(">") + 1);
        String today = sf.format(new Date());
        if(today.equals(dateStr)) {
            log.info("date -> " + dateStr);
            page.putField("update", "HaiZeiWang");
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setCharset(charSet)
                .setTimeOut(10*1000)
                .setRetryTimes(3)
                .setRetrySleepTime(3000);
    }

//    @Scheduled(cron = "0/30 * * ? * SUN")
    public void process() {
        Spider.create(new HaiZeiWangUpdate())
                .addUrl(URL)
                .addPipeline(this.emailDataPipeline)
                .run();
    }

    public static void main(String[] args) {
        Spider.create(new HaiZeiWangUpdate())
                .addUrl(URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))
                .run();
    }
}
