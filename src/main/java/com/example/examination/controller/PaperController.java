package com.example.examination.controller;

import com.example.examination.controller.model.PaperCreateReq;
import com.example.examination.controller.model.PaperPublishReq;
import com.example.examination.controller.model.PaperReq;
import com.example.examination.controller.model.PublishReq;
import com.example.examination.service.PaperService;
import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("paper")
public class PaperController {

    @Resource
    private PaperService paperService;

    @PostMapping("save")
    public Result save(@RequestBody PaperCreateReq req) {
        log.info("保存试卷,req#{}", req);
        Result.ResultBuilder builder = Result.builder();
        try {
            paperService.save(req);
        } catch (Exception e) {
            log.error("保存试卷失败", e);
            builder.code(-1).msg("保存试卷失败");
        }
        return builder.build();
    }

    @PostMapping("publish")
    public Result publish(@RequestBody PaperPublishReq req) {
        log.info("发布试卷,req#{}", req);
        Result.ResultBuilder builder = Result.builder();
        try {
            paperService.publish(req);
        } catch (Exception e) {
            log.error("发布试卷失败", e);
            builder.code(-1).msg("发布试卷失败");
        }
        return builder.build();
    }

    @PostMapping("publish/record")
    public Result publishRecord(@RequestBody PublishReq req) {
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(paperService.getPublishRecord(req));
        } catch (Exception e) {
            log.error("查询发布记录失败", e);
            builder.code(-1).msg("查询发布记录失败");
        }
        return builder.build();
    }

    @PostMapping("get")
    public Result get(@RequestBody PaperReq req) {
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(paperService.get(req));
        } catch (Exception e) {
            log.error("查询试卷失败", e);
            builder.code(-1).msg("查询试卷失败").build();
        }
        return builder.build();
    }

    @PostMapping("get/{paperId}")
    public Result get(@PathVariable Integer paperId) {
        log.info("查询试卷,paperId#{}", paperId);
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(paperService.getByPaperId(paperId));
        } catch (Exception e) {
            log.error("查询试卷失败", e);
            builder.code(-1).msg("查询试卷失败").build();
        }
        return builder.build();
    }
//
//    @RequestMapping("delete/{id}")
//    public Result deleteAccount(@PathVariable Integer id) {
//        log.info("删除试题,id#{}", id);
//        Result.ResultBuilder builder = Result.builder();
//        try {
//            examinationService.delete(id);
//        } catch (Exception e) {
//            log.error("删除试题失败", e);
//            builder.code(-1).msg("删除试题失败").build();
//        }
//        return builder.build();
//    }

}
