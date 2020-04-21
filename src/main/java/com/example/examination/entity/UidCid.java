package com.example.examination.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户id、班级id对应关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UidCid {
    private Integer uid;
    private Integer cid;
}