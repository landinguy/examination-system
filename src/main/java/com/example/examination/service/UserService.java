package com.example.examination.service;

import com.alibaba.fastjson.JSONObject;
import com.example.examination.dao.UserMapper;
import com.example.examination.entity.User;
import com.example.examination.entity.UserExample;
import com.example.examination.util.Consts;
import com.example.examination.util.Result;
import com.example.examination.view.UserReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpSession session;

    public Result checkAndLogin(String username, String password) throws Exception {
        UserExample example = new UserExample();
        example.createCriteria()
                .andUsernameEqualTo(username)
                .andPasswordEqualTo(Base64Utils.encodeToString(password.getBytes("utf-8")));
        List<User> users = userMapper.selectByExample(example);
        if (users.size() > 0) {
            User user = users.get(0);
            JSONObject data = new JSONObject();
            data.fluentPut("username", user.getUsername())
                    .fluentPut("nickname", user.getNickname())
                    .fluentPut("uid", user.getId())
                    .fluentPut("role", user.getRole());

            session.setAttribute(Consts.SEESION_UNAME, user.getUsername());
            session.setAttribute(Consts.SEESION_UID, user.getId());
            session.setMaxInactiveInterval(30 * 60);


            return Result.builder().code(0).msg("success").data(data).build();
        }
        return Result.builder().code(-1).msg("用户名或者密码错误").build();
    }

    public Result add(User user) throws Exception {
        Integer id = user.getId();
        Date now = new Date();
        user.setUpdateTs(now);
        int i;
        if (id != null) {
            user.setPassword(null);
            i = userMapper.updateByPrimaryKeySelective(user);
        } else {
            user.setCreateTs(now);
            user.setPassword(Base64Utils.encodeToString(user.getPassword().getBytes("utf-8")));
            i = userMapper.insertSelective(user);
        }
        if (i > 0) {
            log.info("save or update user successfully,user#{}", user);
            return Result.builder().code(0).msg("success").build();
        }
        return Result.builder().code(-1).msg("fail").build();
    }

    @Transactional
    public Result delete(Integer id) {
        int i = userMapper.deleteByPrimaryKey(id);
        log.info("delete user successfully,userId#{}", id);
        return Result.builder().code(-1).msg("fail").build();
    }

    public Result select(UserReq req) {
        UserExample example = new UserExample();
        long total = userMapper.countByExample(example);
        if (req.getPageNo() != null && req.getPageSize() != null) {
            example.setPageNo((req.getPageNo() - 1) * req.getPageSize());
            example.setPageSize(req.getPageSize());
        }
//        UserExample.Criteria criteria = example.createCriteria();
//        if (!StringUtils.isEmpty(req.getUsername())) {
//            criteria.andUsernameLike(req.getUsername() + "%");
//        }
        JSONObject data = new JSONObject();
        List<User> users = userMapper.selectByExample(example);
        users.forEach(u -> u.setPassword(""));
        data.fluentPut("list", users).fluentPut("total", total);
        return Result.builder().msg("success").data(data).build();
    }

    /*** 修改密码 ***/
    public Result updatePassword(String oldPassword, String newPassword) throws Exception {
        Integer id = (Integer) session.getAttribute(Consts.SEESION_UID);
        if (id != null) {
            User user = userMapper.selectByPrimaryKey(id);
            if (!user.getPassword().equals(Base64Utils.encodeToString(oldPassword.getBytes("utf-8")))) {
                return Result.builder().code(-1).msg("旧密码错误").build();
            }
            user.setPassword(Base64Utils.encodeToString(newPassword.getBytes("utf-8")));
            int i = userMapper.updateByPrimaryKeySelective(user);
            if (i > 0) {
                return Result.builder().code(0).msg("success").build();
            }
        }
        return Result.builder().code(-1).msg("fail").build();
    }

    private int countByRole(String role) {
        UserExample example = new UserExample();
        if (role != null) example.createCriteria().andRoleEqualTo(role);
        return (int) userMapper.countByExample(example);
    }

    private String parse(LocalDateTime localDateTime) {
        long l = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return String.valueOf(l);
    }
}
