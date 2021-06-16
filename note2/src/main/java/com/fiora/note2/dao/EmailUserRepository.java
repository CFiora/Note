package com.fiora.note2.dao;

import com.fiora.note2.model.EmailUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailUserRepository extends JpaRepository<EmailUser, Long> {
    List<EmailUser> findByEmailId(Long emailId);
}
