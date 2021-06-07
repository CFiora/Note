package com.fiora.note2.webmagic;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class HaiZeiWangUpdate implements PageProcessor {
    public static final String charSet = "utf-8";
    public static final String URL = "https://www.iqiyi.com/a_19rrhb3xvl.html";
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void process(Page page) {
        log.info("URL -> " + page.getUrl());
        String span = page.getHtml().css("span.title-update-progress a i.title-update-num").get();
        log.info("span -> " + span);
        String subSpan = span.replace("</i>","");
        String dateStr = subSpan.substring(subSpan.indexOf(">") + 1);
        log.info("date -> " + dateStr);
        String today = sf.format(new Date());
        log.info("today -> " + today);
        System.out.println(today.equals(dateStr));
//        sendEmail();
    }

    @Override
    public Site getSite() {
        return Site.me().setCharset(charSet)
                .setTimeOut(10*1000)
                .setRetryTimes(3)
                .setRetrySleepTime(3000);
    }

    public static void main(String[] args) {
        Spider.create(new HaiZeiWangUpdate())
                .addUrl(URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))
                .run();
    }
}
