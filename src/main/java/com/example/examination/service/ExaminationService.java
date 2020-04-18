package com.example.examination.service;

import com.alibaba.fastjson.JSONObject;
import com.example.examination.controller.model.ExaminationReq;
import com.example.examination.dao.ExaminationMapper;
import com.example.examination.entity.Examination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ExaminationService {
    @Resource
    private ExaminationMapper examinationMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private UserService userService;

    public void save(Examination examination) {
        Integer id = examination.getId();
        examination.setCreatorId(commonService.getUserId());
        if (id == null) {
            examinationMapper.insertSelective(examination);
        } else {
            examinationMapper.updateByPrimaryKeySelective(examination);
        }
    }

    public JSONObject get(ExaminationReq req) {
        if (req.getPageNo() == null) req.setPageNo(1);
        if (req.getPageSize() == null) req.setPageSize(10);
        Integer userId = commonService.getUserId();
        Optional.ofNullable(userService.getById(userId)).ifPresent(it -> {
            String role = it.getRole();
            if (role.equals("TEACHER") || role.equals("COMPANY")) req.setCreatorId(userId);
        });
        log.info("查询试题,req#{}", req);
        Integer total = examinationMapper.count(req);
        List<Examination> list = examinationMapper.select(req);
        return new JSONObject().fluentPut("total", total).fluentPut("list", list);
    }

    public void delete(Integer id) {
        examinationMapper.deleteByPrimaryKey(id);
    }
}
