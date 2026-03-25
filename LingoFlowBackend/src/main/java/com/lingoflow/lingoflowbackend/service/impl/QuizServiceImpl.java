package com.lingoflow.lingoflowbackend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingoflow.lingoflowbackend.model.dto.QuizSubmitRequest;
import com.lingoflow.lingoflowbackend.model.entity.Article;
import com.lingoflow.lingoflowbackend.model.vo.QuizFeedbackVO;
import com.lingoflow.lingoflowbackend.model.vo.QuizQuestionVO;
import com.lingoflow.lingoflowbackend.service.ArticleService;
import com.lingoflow.lingoflowbackend.service.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<QuizQuestionVO> generateQuiz(Long userId, Long articleId) {
        Article article = articleService.getById(articleId);
        if (article == null || !article.getUserId().equals(userId)) {
            throw new RuntimeException("文章不存在或无权访问");
        }

        String prompt = String.format("""
            你是一位专业的英语老师。请根据以下这篇英文文章，生成 3 道单项选择题（包含 1 道主旨大意题和 2 道细节理解题），用来测试学生的阅读理解。
            
            你必须且只能返回一个合法的 JSON 数组，绝不允许输出任何 Markdown 标记（如 ```json）或多余的解释文字。
            
            JSON 格式严格如下：
            [
              {
                "question": "英文题目内容",
                "options": [
                  "A. 选项A内容",
                  "B. 选项B内容",
                  "C. 选项C内容",
                  "D. 选项D内容"
                ]
              }
            ]
            
            【文章内容】：
            %s
            """, article.getAdaptedText());

        String aiResponse = chatClient.call(prompt).replace("```json", "").replace("```", "").trim();

        try {
            return objectMapper.readValue(aiResponse, new TypeReference<List<QuizQuestionVO>>() {});
        } catch (Exception e) {
            log.error("自测题生成解析失败: " + aiResponse, e);
            throw new RuntimeException("AI 出题失败，请重试");
        }
    }

    @Override
    public QuizFeedbackVO submitAndEvaluate(Long userId, QuizSubmitRequest request) {
        Article article = articleService.getById(request.getArticleId());
        if (article == null || !article.getUserId().equals(userId)) {
            throw new RuntimeException("文章不存在或无权访问");
        }

        try {
            String userAnswersStr = objectMapper.writeValueAsString(request.getUserAnswers());

            String prompt = String.format("""
                你是一位耐心且专业的英语老师。学生刚刚阅读了一篇英文文章，并完成了一组测试题。
                请根据文章内容，批改学生的答案，并给出总得分（满分100分，共3题）以及详细的解析。
                
                为了契合“i+1”学习法，请你同时提供【中文详细解析】和【符合 %s 难度的纯英文解析】。
                
                【极其重要的指令】：
                在下方的“学生作答情况”中，包含了该题目的【所有原选项 (originalOptions)】。
                你在给出正确答案和解析时，**必须严格引用原选项中的原文**，绝对不允许自己凭空捏造或重新生成新的选项内容！
                
                你必须且只能返回一个合法的 JSON 对象，绝不允许输出任何 Markdown 标记（如 ```json）或废话。
                
                JSON 格式严格如下：
                {
                  "score": 100,
                  "feedbacks": [
                    {
                      "question": "题目内容",
                      "userAnswer": "学生选的答案",
                      "isCorrect": true,
                      "explanation": "中文详细解析：指出正确答案是哪一个原选项，为什么选这个，以及在文章哪一句能找到依据。",
                      "englishExplanation": "英文详细解析：与中文解析意思一致，但必须使用完全符合 %s 难度的英文词汇和语法来编写。"
                    }
                  ]
                }
                
                【文章内容】：
                %s
                
                【学生作答情况】：
                %s
                """,
                    article.getDifficultyLevel(),
                    article.getDifficultyLevel(),
                    article.getAdaptedText(),
                    userAnswersStr);

            String aiResponse = chatClient.call(prompt).replace("```json", "").replace("```", "").trim();
            return objectMapper.readValue(aiResponse, QuizFeedbackVO.class);

        } catch (Exception e) {
            log.error("AI批改解析失败", e);
            throw new RuntimeException("AI 批改失败，请稍后重试");
        }
    }
}