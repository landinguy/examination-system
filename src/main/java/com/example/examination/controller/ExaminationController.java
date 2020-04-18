package com.example.examination.controller;

import com.example.examination.controller.model.ExaminationReq;
import com.example.examination.entity.Examination;
import com.example.examination.service.ExaminationService;
import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("examination")
public class ExaminationController {

    @Resource
    private ExaminationService examinationService;

    @PostMapping("save")
    public Result save(@RequestBody Examination examination) {
        log.info("保存试题,examination#{}", examination);
        Result.ResultBuilder builder = Result.builder();
        try {
            examinationService.save(examination);
        } catch (Exception e) {
            log.error("保存试题失败", e);
            builder.code(-1).msg("保存试题失败");
        }
        return builder.build();
    }

    @PostMapping("get")
    public Result get(@RequestBody ExaminationReq req) {
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(examinationService.get(req));
        } catch (Exception e) {
            log.error("查询试题失败", e);
            builder.code(-1).msg("查询试题失败").build();
        }
        return builder.build();
    }

    @RequestMapping("delete/{id}")
    public Result deleteAccount(@PathVariable Integer id) {
        log.info("删除试题,id#{}", id);
        Result.ResultBuilder builder = Result.builder();
        try {
            examinationService.delete(id);
        } catch (Exception e) {
            log.error("删除试题失败", e);
            builder.code(-1).msg("删除试题失败").build();
        }
        return builder.build();
    }

}
