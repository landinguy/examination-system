package com.example.examination.controller.model;

import lombok.Data;

@Data
public class PaperReq {
    private Integer pageNo;
    private Integer pageSize;
    private Integer creatorId;
}
