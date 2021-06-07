package com.fiora.note2.service.impl;
import com.fiora.note2.dao.JobInfoRepository;
import com.fiora.note2.model.JobInfo;
import com.fiora.note2.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {
    @Autowired
    private JobInfoRepository repository;


    @Override
    @Transactional
    public void save(JobInfo jobInfo) {
        // 根据url和time查询数据
        JobInfo param = new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        List<JobInfo> list = this.findJobInfo(param);
        if(list.isEmpty()) {
            repository.saveAndFlush(jobInfo);
        }
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        Example example = Example.of(jobInfo);
        return repository.findAll(example);
    }

    @Override
    public JobInfo findLastJobInfo() {
        return repository.findLastJobInfo();
    }

    @Override
    public List<JobInfo> findByIdBetween(Long start, Long end) {
        return repository.findAllByIdBetween(start, end);
    }
}
