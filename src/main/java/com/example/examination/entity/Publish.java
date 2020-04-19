package com.example.examination.entity;

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
public class Publish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer publisherId;//发布者
    private Integer paperId;
    private String publishTs;//发布时间
    @Builder.Default
    private boolean limitAnswerTime = false;//是否限制答题时间
    private Long answerTs;//答题时间
}