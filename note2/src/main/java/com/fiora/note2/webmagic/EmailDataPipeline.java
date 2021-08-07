package com.fiora.note2.webmagic;

import com.fiora.note2.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class EmailDataPipeline implements Pipeline {
    @Autowired
    private EmailService emailService;

    @Override
    public void process(ResultItems resultItems, Task task) {

        String update = resultItems.get("update");
        // 判空
        if (update != null) {
            // 发送
            switch (update) {
                case "YiRenZhiXia": emailService.sendEmail("YiRenZhiXia"); break;
                case "HaiZeiWang": emailService.sendEmail("HaiZeiWang"); break;
                case "Conan": emailService.sendEmail("Conan"); break;
                default:
                    ;
            }
        }
    }
}
