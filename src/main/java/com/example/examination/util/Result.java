package com.example.examination.util;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by xxf on 2018/12/1 0001.
 */
@Data
@Builder
@ToString
public class Result {
    private int code = 0;
    private String msg;
    private Object data;
}
