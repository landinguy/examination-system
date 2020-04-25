package com.example.examination.util;

import lombok.Data;

/**
 * @author landing guy
 * @date 2020/4/25
 */
@Data
public class Analysis {
    private int bad;
    private int normal;
    private int good;

    public void addBad() {
        this.bad++;
    }

    public void addNormal() {
        this.normal++;
    }

    public void addGood() {
        this.good++;
    }
}
