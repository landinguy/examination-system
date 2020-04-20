package com.example.examination.dao;

import com.example.examination.controller.model.PublishReq;
import com.example.examination.entity.Publish;

import java.util.List;

public interface PublishMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Publish record);

    int insertSelective(Publish record);

    Publish selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Publish record);

    int updateByPrimaryKey(Publish record);

    int count(PublishReq req);

    List<Publish> select(PublishReq req);
}