package com.fiora.note2.webmagic;


import com.fiora.note2.model.JobInfo;
import com.fiora.note2.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDataPipeline implements Pipeline {
    @Autowired
    private JobInfoService service;

    @Override
    public void process(ResultItems resultItems, Task task) {
        // 获取封装好的jobInfo对象
        JobInfo jobInfo = resultItems.get("jobInfo");
        // 判空
        if (jobInfo != null) {
            // 保存
            service.save(jobInfo);
        }

    }
}
