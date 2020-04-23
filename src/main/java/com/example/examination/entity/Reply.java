package com.example.examination.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 答题记录
 */
@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer paperId;//试卷id
    private Integer publishId;//发布记录id
    private Integer userId;//答题人id
    private String submitTs;//试卷提交时间
    private String correct;//正确题id
    private String error;//错误题id
    private String answer;//答案
    private Integer score;//得分
}