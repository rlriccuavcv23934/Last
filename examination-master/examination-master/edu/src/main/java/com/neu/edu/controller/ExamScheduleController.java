package com.neu.edu.controller;

import com.neu.edu.dto.ExamScheduleDTO;
import com.neu.edu.service.ExamScheduleService;
import com.neu.edu.utils.ResultModel;
import com.neu.edu.vo.ExamScheduleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ExamScheduleController {

    @Autowired
    private ExamScheduleService examScheduleService;

    @GetMapping("")
    public ResultModel<List<ExamScheduleVO>> findAll(){
        return examScheduleService.findAll();
    }

    @GetMapping("/{exam_name}")
    public ResultModel<List<ExamScheduleVO>> findByName(@PathVariable("exam_name") String exam_name){
        try {
            System.out.println(exam_name);
            String decodedExamName = URLDecoder.decode(exam_name.replace("+", " "), "UTF-8");
            System.out.println(decodedExamName);
            return examScheduleService.findByName(decodedExamName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("")
    public ResultModel add(ExamScheduleDTO examScheduleDTO){
        return examScheduleService.add(examScheduleDTO);
    }

    @DeleteMapping("/{id}")
    public ResultModel deleteById(@PathVariable("id") int teacher_id){
        return examScheduleService.deleteById(teacher_id);//TODO 这里也需要进行写更改
    }

    @PutMapping("/{id}")
    public ResultModel updateById(@PathVariable("id") int teacher_id,String name){
        return examScheduleService.updateById(teacher_id,name);
        //TODO: 由于传入的id为 “http://localhost:8080/edu/schedules/5”(我的端口是8090不用在意)
        //TODO: 而我传入的id是course_id，所以这里的id应该是course_id，而不是teacher_id
        //TODO: 而不仅是Controller中的teacher_id还有mapper里的和service的接口和实现类的形参都需要改
        //TODO: 问过老师了形参也挺重要的，需要你更改下测试下
    }
}
