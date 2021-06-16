package com.fiora.note2.dao;

import com.fiora.note2.model.EmailRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailRecordRepository extends JpaRepository<EmailRecord, Long> {
    @Query(value = "select * from email_record where email_id = ?1 and create_time > ?2 and create_time < ?3", nativeQuery = true)
    List<EmailRecord> findByEmailIdAndCreateTimeBetween(Long emailId, LocalDateTime start, LocalDateTime end);
}
