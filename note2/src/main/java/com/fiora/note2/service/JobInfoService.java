package com.fiora.note2.service;


import com.fiora.note2.model.JobInfo;

import java.util.List;

public interface JobInfoService {
    void save(JobInfo jobInfo);

    List<JobInfo> findJobInfo(JobInfo jobInfo);

    JobInfo findLastJobInfo();

    List<JobInfo> findByIdBetween(Long start, Long end);
}
