package com.example.examination.service;

import com.alibaba.fastjson.JSONObject;
import com.example.examination.controller.model.UserReq;
import com.example.examination.dao.UserMapper;
import com.example.examination.entity.User;
import com.example.examination.util.Consts;
import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private HttpSession httpSession;

    public Result checkAndLogin(String username, String password) {
        User select = userMapper.selectByUsernameAndPassword(username, password);
        if (select != null) {
            JSONObject data = new JSONObject();
            data.fluentPut("username", select.getUsername()).fluentPut("uid", select.getId()).fluentPut("role", select.getRole());
            httpSession.setAttribute(Consts.SEESION_UNAME, select.getUsername());
            httpSession.setAttribute(Consts.SEESION_UID, select.getId());
            httpSession.setMaxInactiveInterval(30 * 60);
            return Result.builder().data(data).build();
        }
        return Result.builder().code(-1).msg("用户名或者密码错误").build();
    }

    public void add(User user) {
        Integer id = user.getId();
        if (id != null) {
            userMapper.updateByPrimaryKeySelective(user);
        } else {
            userMapper.insertSelective(user);
        }
    }

    public Result delete(Integer id) {
        int i = userMapper.deleteByPrimaryKey(id);
        log.info("delete user successfully,userId#{}", id);
        return Result.builder().code(-1).msg("fail").build();
    }

    public Object select(UserReq req) {
        Integer total = userMapper.count(req);
        List<User> list = userMapper.select(req);
        return new JSONObject().fluentPut("total", total).fluentPut("list", list);
    }

    /*** 修改密码 ***/
    public Result updatePassword(String oldPassword, String newPassword) {
        Result.ResultBuilder builder = Result.builder();
        Integer id = (Integer) httpSession.getAttribute(Consts.SEESION_UID);
        if (id != null) {
            User user = userMapper.selectByPrimaryKey(id);
            if (!user.getPassword().equals(oldPassword)) {
                builder.code(-1).msg("旧密码错误");
            } else {
                user.setPassword(newPassword);
                userMapper.updateByPrimaryKeySelective(user);
            }
        }
        return builder.build();
    }

    public void updatePassword(Integer id, String password) {
        Optional.ofNullable(userMapper.selectByPrimaryKey(id)).ifPresent(it -> {
            it.setPassword(password);
            userMapper.updateByPrimaryKeySelective(it);
        });
    }

    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
