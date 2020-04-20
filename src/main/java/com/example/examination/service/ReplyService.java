package com.example.examination.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.examination.controller.model.ReplyCreateReq;
import com.example.examination.controller.model.ReplyReq;
import com.example.examination.dao.ExaminationMapper;
import com.example.examination.dao.PaperMapper;
import com.example.examination.dao.ReplyMapper;
import com.example.examination.entity.Reply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReplyService {
    @Resource
    private PaperMapper paperMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private UserService userService;
    @Resource
    private ExaminationMapper examinationMapper;
    @Resource
    private ReplyMapper replyMapper;

    public void save(ReplyCreateReq req) {
        List<String> correct = new ArrayList<>();
        List<String> error = new ArrayList<>();
        Optional.ofNullable(req.getAnswer()).ifPresent(answer -> {
            Map<String, String> map = JSON.parseObject(answer, new TypeReference<Map<String, String>>() {
            });
            map.forEach((id, value) ->
                    Optional.ofNullable(examinationMapper.selectByPrimaryKey(Integer.valueOf(id))).ifPresent(it -> {
                        if (value.equals(it.getAnswer())) correct.add(id);
                        else error.add(id);
                    })
            );
        });
        Reply build = Reply.builder().paperId(req.getPaperId()).publishId(req.getPublishId()).answer(req.getAnswer()).userId(commonService.getUserId())
                .submitTs(commonService.getDateTime()).correct(String.join(",", correct)).error(String.join(",", error)).build();
        replyMapper.insertSelective(build);
    }


    public Object get(ReplyReq req) {
        if (req.getPageNo() == null) req.setPageNo(1);
        if (req.getPageSize() == null) req.setPageSize(10);
        Integer userId = commonService.getUserId();
        Optional.ofNullable(userService.getById(userId)).ifPresent(it -> {
            String role = it.getRole();
            if (role.equals("STUDENT") || role.equals("INTERVIEWER")) req.setUserId(userId);
        });
        log.info("查询答题记录,req#{}", req);
        Integer total = replyMapper.count(req);
        List<JSONObject> list = replyMapper.select(req).stream().map(it -> {
            JSONObject jo = JSON.parseObject(JSON.toJSONString(it));
            Optional.ofNullable(userService.getById(it.getUserId())).ifPresent(user -> jo.put("username", user.getUsername()));
            Optional.ofNullable(paperMapper.selectByPrimaryKey(it.getPaperId())).ifPresent(paper -> jo.put("title", paper.getTitle()));
            jo.put("correctNum", Stream.of(it.getCorrect().split(",")).count());
            jo.put("errorNum", Stream.of(it.getError().split(",")).count());
            return jo;
        }).collect(Collectors.toList());
        return new JSONObject().fluentPut("total", total).fluentPut("list", list);
    }

}