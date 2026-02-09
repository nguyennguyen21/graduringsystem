package com.devn.studentslearn.service.implement;

import com.devn.studentslearn.dto.request.SubmitExamRequestDTO;

public interface IExamSubmissionService {
    String submitExam(SubmitExamRequestDTO request);
}