package com.example.examination.controller.model;

import lombok.Data;

@Data
public class PublishReq {
    private Integer pageNo;
    private Integer pageSize;
    private Integer publisherId;
}