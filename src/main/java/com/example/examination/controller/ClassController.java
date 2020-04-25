package com.example.examination.controller;

import com.example.examination.service.ClassService;
import com.example.examination.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("class")
public class ClassController {
    @Resource
    private ClassService classService;

    @GetMapping("save")
    public Result save(String classname) {
        log.info("保存班级,classname#{}", classname);
        Result.ResultBuilder builder = Result.builder();
        try {
            return classService.save(classname);
        } catch (Exception e) {
            log.error("保存班级失败", e);
            builder.code(-1).msg("保存班级失败");
        }
        return builder.build();
    }

    @GetMapping("get")
    public Result get() {
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(classService.get());
        } catch (Exception e) {
            log.error("查询班级失败", e);
            builder.code(-1).msg("查询班级失败").build();
        }
        return builder.build();
    }

    @GetMapping("student/save")
    public Result saveStudent(Integer classId, String name, String accountName) {
        log.info("添加学生,classId#{},name#{},accountName#{}", classId, name, accountName);
        Result.ResultBuilder builder = Result.builder();
        try {
            return classService.saveStudent(classId, name, accountName);
        } catch (Exception e) {
            log.error("添加学生失败", e);
            builder.code(-1).msg("添加学生失败").build();
        }
        return builder.build();
    }

    @GetMapping("student/get/{classId}")
    public Result getClassStudent(@PathVariable Integer classId) {
        log.info("查询班级学生,classId#{}", classId);
        Result.ResultBuilder builder = Result.builder();
        try {
            builder.data(classService.getClassStudent(classId));
        } catch (Exception e) {
            log.error("查询班级学生失败", e);
            builder.code(-1).msg("查询班级学生失败").build();
        }
        return builder.build();
    }

    @GetMapping("student/delete/{id}")
    public Result deleteStudent(@PathVariable Integer id) {
        log.info("删除学生,id#{}", id);
        Result.ResultBuilder builder = Result.builder();
        try {
            classService.deleteStudent(id);
        } catch (Exception e) {
            log.error("删除学生失败", e);
            builder.code(-1).msg("删除学生失败").build();
        }
        return builder.build();
    }

    @GetMapping("apply")
    public Result apply(Integer userId, Integer classId) {
        log.info("班级申请,userId#{},classId#{}", userId, classId);
        Result.ResultBuilder builder = Result.builder();
        try {
            return classService.apply(userId, classId);
        } catch (Exception e) {
            log.error("班级申请失败", e);
            builder.code(-1).msg("班级申请失败").build();
        }
        return builder.build();
    }

}