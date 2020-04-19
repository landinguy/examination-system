package com.example.examination.dao;

import com.example.examination.controller.model.PaperReq;
import com.example.examination.entity.Paper;

import java.util.List;

public interface PaperMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Paper record);

    int insertSelective(Paper record);

    Paper selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Paper record);

    int updateByPrimaryKey(Paper record);

    Integer count(PaperReq req);

    List<Paper> select(PaperReq req);
}