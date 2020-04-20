package com.example.examination.controller.model;

import lombok.Data;

@Data
public class ReplyReq {
    private Integer pageNo;
    private Integer pageSize;
    private Integer userId;//答题人id
    private Integer paperId;
}