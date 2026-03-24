package com.lingoflow.lingoflowbackend.controller;

import com.lingoflow.lingoflowbackend.common.Result;
import com.lingoflow.lingoflowbackend.model.dto.ArticleGenerateRequest;
import com.lingoflow.lingoflowbackend.model.vo.ArticleVO;
import com.lingoflow.lingoflowbackend.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 提交原文，AI 智能改写并提取生词
     */
    @PostMapping("/generate")
    public Result<ArticleVO> generateArticle(@RequestBody ArticleGenerateRequest request, HttpServletRequest httpServletRequest) {
        // 从 JWT 拦截器中取出我们之前放进去的 userId
        Long userId = (Long) httpServletRequest.getAttribute("userId");

        // 调用核心业务逻辑
        ArticleVO articleVO = articleService.generateAndSaveArticle(userId, request);

        return Result.success(articleVO);
    }
}