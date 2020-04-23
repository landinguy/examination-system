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
            classMapper.insertSelective(Class.builder().classname(classname).build());
        }
        return builder.build();
    }


    public Object get() {
        return classMapper.select();
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

    public void saveStudent(Integer classId, String name) {
        User user = User.builder().username(name).role("STUDENT").build();
        userMapper.insertSelective(user);
        Optional.ofNullable(user.getId()).ifPresent(userId -> {
            log.info("userId#{}", userId);
            UidCid uidCid = UidCid.builder().uid(userId).cid(classId).build();
            uidCidMapper.insertSelective(uidCid);
        });
    }

    public void deleteStudent(Integer id) {
        uidCidMapper.deleteByPrimaryKey(id);
    }
}