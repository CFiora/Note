package com.fiora.note2.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class EmailUser {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;
    private Long emailId;
    private Long userId;
}
