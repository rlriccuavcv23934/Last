package com.neu.edu.service;

import com.neu.edu.dto.ExamScheduleDTO;
import com.neu.edu.utils.ResultModel;
import com.neu.edu.vo.ExamScheduleVO;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author <a href="https://github.com/HCDUO">HCDUO</a>
 * @date:2023/7/13 16:15
 */
public interface ExamScheduleService {
    ResultModel<List<ExamScheduleVO>> findAll();

    ResultModel<List<ExamScheduleVO>> findByName(String exam_name);

    ResultModel add(ExamScheduleDTO examScheduleDTO);

    ResultModel deleteById(int course_id);

    ResultModel updateById(int course_id, String name);//TODO: course_id
}
