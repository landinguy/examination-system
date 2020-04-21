package com.example.examination.controller;

import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Slf4j
@RestController
public class CommonController {

    @GetMapping("backup")
    public boolean backup() {
        log.info("开始备份数据...");
        try {
            String command = "cmd /c mysqldump -u root -proot examination > D:/examination.sql";
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            log.error("备份数据失败", e);
            return false;
        }
        return true;
    }

    @GetMapping("restore")
    public Result restore() {
        log.info("开始恢复数据...");
        Result.ResultBuilder builder = Result.builder();
        try {
            File file = new File("D:/examination.sql");
            if (!file.exists()) {
                log.warn("sql文件不存在！");
                builder.code(-1).msg("sql文件不存在");
            } else {
                String command = "cmd /c mysql -u root -proot examination < D:/examination.sql";
                Runtime.getRuntime().exec(command);
            }
        } catch (Exception e) {
            log.error("恢复数据失败", e);
            builder.code(-1).msg("恢复数据失败");
        }
        return builder.build();
    }

}