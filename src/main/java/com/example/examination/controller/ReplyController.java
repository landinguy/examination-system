package com.example.examination.controller;

import com.example.examination.controller.model.ReplyCreateReq;
import com.example.examination.controller.model.ReplyReq;
import com.example.examination.service.ReplyService;
import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("reply")
public class ReplyController {
    @Resource
    private ReplyService replyService;

    @PostMapping("save")
    public Result save(@RequestBody ReplyCreateReq req) {
        log.info("保存答题内容,req#{}", req);
        Result.ResultBuilder builder = Result.builder();
        try {
            replyService.save(req);
        } catch (Exception e) {
            log.error("保存答题内容失败", e);
            builder.code(-1).msg("保存答题内容失败");
        }
        return builder.build();
    }

    @PostMapping("get")
    public Result get(@RequestBody ReplyReq req) {
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(replyService.get(req));
        } catch (Exception e) {
            log.error("查询答题记录失败", e);
            builder.code(-1).msg("查询答题记录失败").build();
        }
        return builder.build();
    }

    @GetMapping("analysis/{publishId}")
    public Result analysis(@PathVariable Integer publishId) {
        log.info("查询班级答题分析结果,publishId#{}", publishId);
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(replyService.analysis(publishId));
        } catch (Exception e) {
            log.error("查询班级答题分析结果失败", e);
            builder.code(-1).msg("查询班级答题分析结果失败").build();
        }
        return builder.build();
    }

}