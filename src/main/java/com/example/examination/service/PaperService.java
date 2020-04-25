package com.example.examination.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.examination.controller.model.PaperCreateReq;
import com.example.examination.controller.model.PaperPublishReq;
import com.example.examination.controller.model.PaperReq;
import com.example.examination.controller.model.PublishReq;
import com.example.examination.dao.ExaminationMapper;
import com.example.examination.dao.PaperMapper;
import com.example.examination.dao.PublishMapper;
import com.example.examination.entity.Examination;
import com.example.examination.entity.Paper;
import com.example.examination.entity.Publish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class PaperService {
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private UserService userService;
    @Resource
    private PublishMapper publishMapper;
    @Resource
    private ExaminationMapper examinationMapper;

    public void save(PaperCreateReq req) {
        int score = 0;
        for (String id : req.getExaminations()) {
            Examination examination = examinationMapper.selectByPrimaryKey(Integer.valueOf(id));
            if (examination != null) score += examination.getDifficulty();
        }
        Paper build = Paper.builder().title(req.getTitle()).createTs(LocalDate.now().toString()).status(1)
                .creatorId(commonService.getUserId()).examinations(String.join(",", req.getExaminations())).score(score).build();
        paperMapper.insertSelective(build);
    }

    public void publish(PaperPublishReq req) {
        Integer paperId = req.getPaperId();
        String datetime = LocalDateTime.now().withNano(0).toString().replace("T", " ");
        Publish build = Publish.builder().paperId(paperId).publisherId(commonService.getUserId())
                .publishTs(datetime).limitAnswerTime(req.isLimitAnswerTime()).answerTs(req.getAnswerTs()).endTs(req.getEndTs()).build();
        int i = publishMapper.insertSelective(build);
        if (i > 0) {
            Paper paper = Paper.builder().id(paperId).status(2).publishTs(datetime).build();
            paperMapper.updateByPrimaryKeySelective(paper);
        }
    }

    public Object get(PaperReq req) {
        if (req.getPageNo() == null) req.setPageNo(1);
        if (req.getPageSize() == null) req.setPageSize(10);
        Integer userId = commonService.getUserId();
        Optional.ofNullable(userService.getById(userId)).ifPresent(it -> {
            String role = it.getRole();
            if (role.equals("TEACHER") || role.equals("COMPANY")) req.setCreatorId(userId);
        });
        log.info("查询试卷,req#{}", req);
        Integer total = paperMapper.count(req);
        List<JSONObject> list = paperMapper.select(req).stream().map(it -> {
            JSONObject jo = JSON.parseObject(JSON.toJSONString(it));
            List<Examination> examinations = new ArrayList<>();
            Stream.of(it.getExaminations().split(",")).map(Integer::valueOf).forEach(id ->
                    Optional.ofNullable(examinationMapper.selectByPrimaryKey(id)).ifPresent(examinations::add)
            );
            jo.put("examinations", examinations);
            return jo;
        }).collect(Collectors.toList());
        return new JSONObject().fluentPut("total", total).fluentPut("list", list);
    }

    public JSONObject getByPaperId(Integer paperId) {
        final JSONObject jo = new JSONObject();
        Optional.ofNullable(paperMapper.selectByPrimaryKey(paperId)).ifPresent(it -> {
            List<Examination> examinations = new ArrayList<>();
            Stream.of(it.getExaminations().split(",")).map(Integer::valueOf).forEach(id ->
                    Optional.ofNullable(examinationMapper.selectByPrimaryKey(id)).ifPresent(examinations::add)
            );
            jo.putAll(JSON.parseObject(JSON.toJSONString(it)));
            jo.put("examinations", examinations);
        });
        return jo;
    }

    public Object getPublishRecord(PublishReq req) {
        if (req.getPageNo() == null) req.setPageNo(1);
        if (req.getPageSize() == null) req.setPageSize(10);
        Integer userId = commonService.getUserId();
        Optional.ofNullable(userService.getById(userId)).ifPresent(it -> {
            String role = it.getRole();
            if (role.equals("TEACHER") || role.equals("COMPANY")) req.setPublisherId(userId);
        });
        log.info("查询发布记录,req#{}", req);
        int total = publishMapper.count(req);
        List<JSONObject> list = publishMapper.select(req).stream().map(it -> {
            JSONObject jo = JSON.parseObject(JSON.toJSONString(it));
            Optional.ofNullable(userService.getById(it.getPublisherId())).ifPresent(user -> jo.put("username", user.getUsername()));
            Optional.ofNullable(paperMapper.selectByPrimaryKey(it.getPaperId())).ifPresent(paper -> jo.put("title", paper.getTitle()));
            return jo;
        }).collect(Collectors.toList());
        return new JSONObject().fluentPut("total", total).fluentPut("list", list);
    }

//    public JSONObject get(ExaminationReq req) {
//        if (req.getPageNo() == null) req.setPageNo(1);
//        if (req.getPageSize() == null) req.setPageSize(10);
//        Integer userId = commonService.getUserId();
//        Optional.ofNullable(userService.getById(userId)).ifPresent(it -> {
//            String role = it.getRole();
//            if (role.equals("TEACHER") || role.equals("COMPANY")) req.setCreatorId(userId);
//        });
//        log.info("查询试题,req#{}", req);
//        Integer total = examinationMapper.count(req);
//        List<Examination> list = examinationMapper.select(req);
//        return new JSONObject().fluentPut("total", total).fluentPut("list", list);
//    }
//
//    public void delete(Integer id) {
//        examinationMapper.deleteByPrimaryKey(id);
//    }
}