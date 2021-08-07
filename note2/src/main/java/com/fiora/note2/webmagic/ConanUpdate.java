package com.fiora.note2.webmagic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class ConanUpdate implements PageProcessor {
    public static final String charSet = "utf-8";
    public static final String URL = "https://ac.qq.com/Comic/comicInfo/id/623654";
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
    @Autowired
    private EmailDataPipeline emailDataPipeline;

    @Override
    public void process(Page page) {
        String span = page.getHtml().css("ul.works-chapter-log span.ui-pl10").toString();
        String subSpan = span.replace("</span>","");
        String dateStr = subSpan.substring(subSpan.indexOf(">") + 1);
        String today = sf.format(new Date());
        if(today.equals(dateStr)) {
            log.info("today -> " + today);
            page.putField("update", "Conan");
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setCharset(charSet)
                .setTimeOut(10*1000)
                .setRetryTimes(3)
                .setRetrySleepTime(3000);
    }

    @Scheduled(cron = "0 0/30 8-20 * * ?")
    public void process() {
        Spider.create(new ConanUpdate())
                .addUrl(URL)
                .addPipeline(this.emailDataPipeline)
                .run();
    }
}
