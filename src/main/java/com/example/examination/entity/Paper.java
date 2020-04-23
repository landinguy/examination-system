package com.example.examination.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer creatorId;
    private Integer status;//1.未发布 2.已发布
    private String title;
    private String createTs;//创建时间
    private String publishTs;//发布时间
    private String examinations;//试题
    private Integer score;//试卷总分
}