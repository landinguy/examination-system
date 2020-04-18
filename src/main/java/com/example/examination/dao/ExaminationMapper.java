package com.example.examination.dao;

import com.example.examination.controller.model.ExaminationReq;
import com.example.examination.entity.Examination;

import java.util.List;

public interface ExaminationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Examination record);

    int insertSelective(Examination record);

    Examination selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Examination record);

    int updateByPrimaryKey(Examination record);

    List<Examination> select(ExaminationReq req);

    Integer count(ExaminationReq req);
}