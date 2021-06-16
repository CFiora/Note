package com.fiora.note2.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Email {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String title;
    private String content;
}
