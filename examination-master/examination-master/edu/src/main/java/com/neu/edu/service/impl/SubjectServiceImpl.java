package com.neu.edu.service.impl;

import com.neu.edu.domain.Subject;
import com.neu.edu.dto.SubjectDTO;
import com.neu.edu.mapper.SubjectMapper;
import com.neu.edu.service.SubjectService;
import com.neu.edu.utils.ResultModel;
import com.neu.edu.vo.SubjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectMapper subjectMapper;

    @Override
    public ResultModel<List<SubjectVO>> findAll() {
        ResultModel<List<SubjectVO>> resultModel = new ResultModel<List<SubjectVO>>();
        List<SubjectVO> subjectVOList = subjectMapper.findAll();
        if (subjectVOList == null) {
            resultModel.setCode(401);
            resultModel.setMsg("查询学科失败");
        } else {
            resultModel.setCode(200);
            resultModel.setMsg("查询学科成功");
            resultModel.setData(subjectVOList);
        }
        return resultModel;
    }

    @Override
    public ResultModel<List<SubjectVO>> findById(int teacher_id) {

        ResultModel<List<SubjectVO>> resultModel = new ResultModel<List<SubjectVO>>();
        List<SubjectVO> subjectVOList = subjectMapper.findById(teacher_id);
        if (subjectVOList == null) {
            resultModel.setCode(401);
            resultModel.setMsg("查询学科失败");
        } else {
            resultModel.setCode(200);
            resultModel.setMsg("查询学科成功");
            resultModel.setData(subjectVOList);
        }
        return resultModel;
    }

    @Override
    public ResultModel add(SubjectDTO subjectDTO) {

        ResultModel resultModel = new ResultModel();

        //DTO--domain
        Subject subject = new Subject();
        subject.setName(subjectDTO.getName());
        subject.setTeacher_id(subjectDTO.getTeacher_id());
        if (subject == null) {
            resultModel.setCode(401);
            resultModel.setMsg("添加学科失败");
        } else {
            subjectMapper.add(subject);
            resultModel.setCode(200);
            resultModel.setMsg("添加学科成功");
        }
        return resultModel;
    }

    @Override
    public ResultModel deleteById(int subject_id) {
        ResultModel resultModel = new ResultModel();
        try {
            if (subjectMapper.deleteById(subject_id) == 0) {
                resultModel.setCode(401);
                resultModel.setMsg("删除学科失败");
            } else {
                resultModel.setCode(200);
                resultModel.setMsg("删除学科成功");
            }
        } catch (Exception e) {
            resultModel.setCode(500);
            resultModel.setMsg("有学生选修，不能删除");
        }
        return resultModel;
    }

    @Override
    public ResultModel updateById(SubjectDTO subjectDTO) {
        ResultModel resultModel = new ResultModel();

        Subject subject = new Subject();
        subject.setName(subjectDTO.getName());
        subject.setSubject_id(subjectDTO.getSubject_id());
        subject.setTeacher_id(subjectDTO.getTeacher_id());

        subjectMapper.updateById(subject);
        if (subject == null) {
            resultModel.setCode(200);
            resultModel.setMsg("更新学科失败");
        } else {
            resultModel.setCode(200);
            resultModel.setMsg("更新学科成功");
        }
        return resultModel;
    }
}
