package com.lingoflow.lingoflowbackend.model.vo;

import lombok.Data;
import java.util.List;

@Data
public class QuizQuestionVO {
    private String question;
    private List<String> options;
}