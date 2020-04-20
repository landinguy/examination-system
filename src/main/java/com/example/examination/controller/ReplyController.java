package com.example.examination.controller;

import com.example.examination.controller.model.ReplyCreateReq;
import com.example.examination.controller.model.ReplyReq;
import com.example.examination.service.ReplyService;
import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}