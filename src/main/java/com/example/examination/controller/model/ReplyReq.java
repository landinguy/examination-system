package com.example.examination.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyReq {
    private Integer pageNo;
    private Integer pageSize;
    private Integer userId;//答题人id
    private Integer paperId;
    private Integer publishId;
}
