package com.example.examination.dao;

import com.example.examination.entity.UidCid;

import java.util.List;

public interface UidCidMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(UidCid record);

    int insertSelective(UidCid record);

    UidCid selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(UidCid record);

    int updateByPrimaryKey(UidCid record);

    UidCid selectByUidAndCid(Integer uid, Integer cid);

    List<UidCid> selectByCid(Integer cid);
}