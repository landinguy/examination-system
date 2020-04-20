package com.example.examination.controller.model;

import lombok.Data;

@Data
public class ReplyCreateReq {
    private Integer paperId;
    private Integer publishId;
    private String answer;
}