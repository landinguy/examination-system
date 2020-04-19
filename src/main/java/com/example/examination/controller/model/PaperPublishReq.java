package com.example.examination.controller.model;

import lombok.Data;

@Data
public class PaperPublishReq {
    private Integer PaperId;
    private boolean limitAnswerTime;
    private Long answerTs;
}
