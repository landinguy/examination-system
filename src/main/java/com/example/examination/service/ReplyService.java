package com.example.examination.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.examination.controller.model.ReplyCreateReq;
import com.example.examination.controller.model.ReplyReq;
import com.example.examination.dao.*;
import com.example.examination.entity.Class;
import com.example.examination.entity.*;
import com.example.examination.util.Analysis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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
    @Resource
    private PublishMapper publishMapper;
    @Resource
    private UidCidMapper uidCidMapper;
    @Resource
    private ClassMapper classMapper;

    public void save(ReplyCreateReq req) {
        List<String> correct = new ArrayList<>();
        List<String> error = new ArrayList<>();
        AtomicInteger score = new AtomicInteger();
        Optional.ofNullable(req.getAnswer()).ifPresent(answer -> {
            Map<String, String> map = JSON.parseObject(answer, new TypeReference<Map<String, String>>() {
            });
            map.forEach((id, value) ->
                    Optional.ofNullable(examinationMapper.selectByPrimaryKey(Integer.valueOf(id))).ifPresent(it -> {
                        if (value.equals(it.getAnswer())) {
                            correct.add(id);
                            score.addAndGet(it.getDifficulty());
                        } else error.add(id);
                    })
            );
        });
        Reply build = Reply.builder().paperId(req.getPaperId()).publishId(req.getPublishId()).answer(req.getAnswer()).userId(commonService.getUserId())
                .submitTs(commonService.getDateTime()).correct(String.join(",", correct)).error(String.join(",", error)).score(score.get()).build();
        replyMapper.insertSelective(build);
    }


    public Object get(ReplyReq req) {
        if (req.getPageNo() == null) req.setPageNo(1);
        if (req.getPageSize() == null) req.setPageSize(10);
        Integer userId = commonService.getUserId();
        Optional.ofNullable(userService.getById(userId)).ifPresent(it -> {
            String role = it.getRole();
            //学生只能查看自己的答题记录
            if (role.equals("STUDENT") || role.equals("INTERVIEWER")) req.setUserId(userId);
        });
        log.info("查询答题记录,req#{}", req);
        Integer total = replyMapper.count(req);
        List<JSONObject> list = replyMapper.select(req).stream().map(it -> {
            JSONObject jo = JSON.parseObject(JSON.toJSONString(it));
            Optional.ofNullable(userService.getById(it.getUserId())).ifPresent(user -> jo.put("username", user.getUsername()));
            Optional.ofNullable(paperMapper.selectByPrimaryKey(it.getPaperId())).ifPresent(paper -> {
                jo.put("title", paper.getTitle());
                jo.put("totalScore", paper.getScore());
            });
            long correctNum = StringUtils.isEmpty(it.getCorrect()) ? 0 : Stream.of(it.getCorrect().split(",")).count();
            jo.put("correctNum", correctNum);
            long errorNum = StringUtils.isEmpty(it.getError()) ? 0 : Stream.of(it.getError().split(",")).count();
            jo.put("errorNum", errorNum);
            Optional.ofNullable(publishMapper.selectByPrimaryKey(it.getPublishId())).ifPresent(publish -> jo.put("endTs", publish.getEndTs()));
            return jo;
        }).collect(Collectors.toList());
        return new JSONObject().fluentPut("total", total).fluentPut("list", list);
    }

    public Object analysis(Integer publishId) {
        JSONArray ja = new JSONArray();
        List<Examination> examinations = getAnswer(publishId);
        List<Examination> easy = new ArrayList<>();
        List<Examination> medium = new ArrayList<>();
        List<Examination> difficult = new ArrayList<>();
        examinations.forEach(it -> {
            if (it.getDifficulty() == 1 || it.getDifficulty() == 2) easy.add(it);
            if (it.getDifficulty() == 3) medium.add(it);
            if (it.getDifficulty() > 3) difficult.add(it);
        });

        Integer userId = commonService.getUserId();
        uidCidMapper.selectByUid(userId).forEach(it -> {
            JSONObject jo = new JSONObject();
            Class cl = classMapper.selectByPrimaryKey(it.getCid());
            jo.put("className", cl.getClassname());
            AtomicInteger total = new AtomicInteger();
            List<String> students = new ArrayList<>();
            Analysis analysisEasy = new Analysis();
            Analysis analysisMedium = new Analysis();
            Analysis analysisDifficult = new Analysis();

            uidCidMapper.selectByCid(cl.getId()).stream().map(uidCid ->
                    userService.getById(uidCid.getUid())
            ).filter(user -> user.getRole().equals("STUDENT")).forEach(user -> {
                total.getAndIncrement();
                Reply reply = replyMapper.selectByPublishIdAndUserId(publishId, user.getId());
                if (reply == null) {
                    students.add(user.getUsername());//统计未答题学生
                } else {
                    Map<Integer, String> answer = JSON.parseObject(reply.getAnswer(), new TypeReference<Map<Integer, String>>() {
                    });
                    analysis(analysisEasy, easy, answer);
                    analysis(analysisMedium, medium, answer);
                    analysis(analysisDifficult, difficult, answer);
                }
            });
            jo.put("total", total);//班级总人数
            jo.put("students", students);
            jo.put("easy", analysisEasy);
            jo.put("medium", analysisMedium);
            jo.put("difficult", analysisDifficult);
            ja.add(jo);
        });
        return ja;
    }

    /*** 计算一个班级各复杂度题目下，掌握程度为较差，一般，很好的人数 ***/
    private void analysis(Analysis analysis, List<Examination> examinations, Map<Integer, String> answer) {
        int count = 0;
        for (Examination item : examinations) {
            if (item.getAnswer().equals(answer.get(item.getId()))) count++;
        }

        float rate = Integer.valueOf(count).floatValue() / examinations.size();
        if (rate < 0.4) {
            analysis.addBad();
        } else if (rate < 0.8) {
            analysis.addNormal();
        } else {
            analysis.addGood();
        }
    }

    /*** 获取试卷题目 ***/
    private List<Examination> getAnswer(Integer publishId) {
        List<Examination> list = new ArrayList<>();
        Optional.ofNullable(publishMapper.selectByPrimaryKey(publishId)).map(Publish::getPaperId)
                .map(it -> paperMapper.selectByPrimaryKey(it)).map(Paper::getExaminations).ifPresent(it -> {
            Stream.of(it.split(",")).map(Integer::valueOf).forEach(id -> {
                Examination examination = examinationMapper.selectByPrimaryKey(id);
                list.add(examination);
            });
        });
        return list;
    }
}