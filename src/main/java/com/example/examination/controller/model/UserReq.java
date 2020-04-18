package com.example.examination.controller.model;

import lombok.Data;

@Data
public class UserReq {
    private Integer pageNo;
    private Integer pageSize;
    private String username;
}
