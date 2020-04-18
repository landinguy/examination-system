package com.example.examination.controller;

import com.example.examination.controller.model.UserReq;
import com.example.examination.entity.User;
import com.example.examination.service.UserService;
import com.example.examination.util.Consts;
import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("login")
    public Result login(String username, String password) {
        try {
            return userService.checkAndLogin(username, password);
        } catch (Exception e) {
            return Result.builder().code(-1).msg("系统异常").build();
        }
    }

    @GetMapping("logout")
    public Result logout(HttpSession session) {
        if (session != null) {
            session.removeAttribute(Consts.SEESION_UNAME);
            session.removeAttribute(Consts.SEESION_UID);
        }
        return Result.builder().build();
    }

    @PostMapping("addAccount")
    public Result add(@RequestBody User user) {
        log.info("添加或修改用户,user#{}", user);
        Result.ResultBuilder builder = Result.builder();
        try {
            userService.add(user);
        } catch (Exception e) {
            log.error("添加或修改用户失败,user#{}", user);
            builder.code(-1).msg("添加或修改用户失败");
        }
        return builder.build();
    }

    @GetMapping("updatePwd")
    public Result updatePwd(String oldPassword, String newPassword) {
        log.info("修改密码,oldPassword#{},newPassword#{}", oldPassword, newPassword);
        try {
            return userService.updatePassword(oldPassword, newPassword);
        } catch (Exception e) {
            log.error("修改密码失败", e);
            return Result.builder().code(-1).msg("修改密码失败").build();
        }
    }

    @GetMapping("updatePassword")
    public Result updatePassword(Integer id, String password) {
        log.info("修改密码,id#{},password#{}", id, password);
        Result.ResultBuilder builder = Result.builder();
        try {
            userService.updatePassword(id, password);
        } catch (Exception e) {
            log.error("修改密码失败", e);
            builder.code(-1).msg("修改密码失败").build();
        }
        return builder.build();
    }

    @PostMapping("getAccount")
    public Result getAccount(@RequestBody UserReq req) {
        log.info("查询用户,req#{}", req);
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(userService.select(req));
        } catch (Exception e) {
            log.error("查询用户失败", e);
            builder.code(-1).msg("查询用户失败").build();
        }
        return builder.build();
    }

    @RequestMapping("deleteAccount")
    public Result deleteAccount(Integer id) {
        try {
            log.info("delete account,id#{}", id);
            return userService.delete(id);
        } catch (Exception e) {
            log.error("delete account error#{}", e);
            return Result.builder().code(-1).msg("删除用户失败").build();
        }
    }

//    @PostMapping("upload")
//    public Result upload(MultipartFile files, String type) {
//        try {
//            log.info("upload file,filename#{},type#{}", files.getOriginalFilename(), type);
//            String filename = UUID.randomUUID().toString().replaceAll("-", "") + ".txt";
//            FileUtils.copyInputStreamToFile(files.getInputStream(), new File("D:\\vms\\upload\\" + filename));
//            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//            service.schedule(() -> {
//                try (Stream<String> lines = Files.lines(Paths.get("D:\\vms\\upload\\" + filename), StandardCharsets.UTF_8)) {
//                    if (type.equals("vehicle")) {
//                        lines.forEach(item -> vehicleService.add(JSONObject.parseObject(item, Vehicle.class)));
//                    } else if (type.equals("peccancy")) {
//                        lines.forEach(item -> recordService.add(JSONObject.parseObject(item, Record.class)));
//                    }
//                } catch (Exception e) {
//                    log.info("do file#{} error#{}", files.getOriginalFilename(), e);
//                }
//            }, 0, TimeUnit.SECONDS);
//            return Result.builder().code(0).msg("文件上传成功").build();
//        } catch (Exception e) {
//            log.error("upload error#{}", e);
//            return Result.builder().code(-1).msg("文件上传失败").build();
//        }
//    }


}
