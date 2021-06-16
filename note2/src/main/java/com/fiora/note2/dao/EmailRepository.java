package com.fiora.note2.dao;

import com.fiora.note2.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
    public Email findByName(String name);
}
