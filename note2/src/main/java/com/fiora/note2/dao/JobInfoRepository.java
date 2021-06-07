package com.fiora.note2.dao;

import com.fiora.note2.model.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobInfoRepository extends JpaRepository<JobInfo, Long> {
    @Query(nativeQuery = true, value = "select * from job_info p where p.id = (select max(pp.id) from job_info pp)")
    public JobInfo findLastJobInfo();

    List<JobInfo> findAllByIdBetween(Long start, Long end);
}
