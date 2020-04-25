package com.example.examination.dao;

import com.example.examination.controller.model.UserReq;
import com.example.examination.entity.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsernameAndPassword(String username, String password);

    List<User> select(UserReq req);

    Integer count(UserReq req);

    Integer isExist(String accountName);
}