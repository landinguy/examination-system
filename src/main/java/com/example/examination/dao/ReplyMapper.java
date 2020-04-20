package com.example.examination.dao;

import com.example.examination.controller.model.ReplyReq;
import com.example.examination.entity.Reply;

import java.util.List;

public interface ReplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);

    int count(ReplyReq req);

    List<Reply> select(ReplyReq req);
}