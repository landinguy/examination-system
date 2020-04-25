package com.example.examination.service;

import com.alibaba.fastjson.JSONObject;
import com.example.examination.dao.ClassMapper;
import com.example.examination.dao.UidCidMapper;
import com.example.examination.dao.UserMapper;
import com.example.examination.entity.Class;
import com.example.examination.entity.UidCid;
import com.example.examination.entity.User;
import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClassService {
    @Resource
    private CommonService commonService;
    @Resource
    private ClassMapper classMapper;
    @Resource
    private UidCidMapper uidCidMapper;
    @Resource
    private UserMapper userMapper;

    public Result save(String classname) {
        Result.ResultBuilder builder = Result.builder();
        Class select = classMapper.selectByClassname(classname);
        if (select != null) {
            builder.code(-1).msg("班级已存在，请换个班级名！");
        } else {
            Class build = Class.builder().classname(classname).build();
            if (classMapper.insertSelective(build) > 0) {
                UidCid uidCid = UidCid.builder().uid(commonService.getUserId()).cid(build.getId()).build();
                uidCidMapper.insertSelective(uidCid);
            }
        }
        return builder.build();
    }


    public Object get() {
        User user = userMapper.selectByPrimaryKey(commonService.getUserId());
        if (user.getRole().equals("TEACHER")) {
            return uidCidMapper.selectByUid(user.getId()).stream().map(it ->
                    classMapper.selectByPrimaryKey(it.getCid())
            ).collect(Collectors.toList());
        } else {
            return classMapper.select();
        }
    }

    public Result apply(Integer userId, Integer classId) {
        Result.ResultBuilder builder = Result.builder();
        UidCid uidCid = uidCidMapper.selectByPrimaryKey(userId);
        if (uidCid != null) {
            String classname = classMapper.selectByPrimaryKey(uidCid.getCid()).getClassname();
            builder.code(-1).msg("您所在班级为【" + classname + "】,无需申请！");
        } else {
            uidCidMapper.insertSelective(UidCid.builder().uid(userId).cid(classId).build());
        }
        return builder.build();
    }

    public Object getClassStudent(Integer classId) {
        List<JSONObject> list = new ArrayList<>();
        uidCidMapper.selectByCid(classId).forEach(uidCid ->
                Optional.ofNullable(userMapper.selectByPrimaryKey(uidCid.getUid())).ifPresent(user -> {
                    if (user.getRole().equals("STUDENT")) {
                        JSONObject jo = new JSONObject();
                        jo.put("id", user.getId());
                        jo.put("name", user.getUsername());
                        list.add(jo);
                    }
                }));
        return list;
    }

    public Result saveStudent(Integer classId, String name, String accountName) {
        Result.ResultBuilder builder = Result.builder();
        if (userMapper.isExist(accountName) > 0) {
            builder.code(-1).msg("账号名已存在！");
        } else {
            User user = User.builder().username(name).accountName(accountName).role("STUDENT").build();
            userMapper.insertSelective(user);
            Optional.ofNullable(user.getId()).ifPresent(userId -> {
                log.info("userId#{}", userId);
                UidCid uidCid = UidCid.builder().uid(userId).cid(classId).build();
                uidCidMapper.insertSelective(uidCid);
            });
        }
        return builder.build();
    }

    public void deleteStudent(Integer id) {
        uidCidMapper.deleteByPrimaryKey(id);
    }
}