package com.example.examination.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table
@Entity
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer creatorId;
    private String content;
    private String answer;
    private Integer difficulty;
    private Integer type;
}