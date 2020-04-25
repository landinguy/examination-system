package com.example.examination.controller.model;

import lombok.Data;

@Data
public class ExaminationReq {
    private Integer type;
    private Integer difficulty;
    private String keyword;
    private Integer creatorId;
    private Integer pageNo;
    private Integer pageSize;
}
