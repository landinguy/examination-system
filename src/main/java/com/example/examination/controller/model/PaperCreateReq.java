package com.example.examination.controller.model;

import lombok.Data;

import java.util.List;

@Data
public class PaperCreateReq {
    private String title;
    private List<String> examinations;
}
