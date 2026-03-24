package com.lingoflow.lingoflowbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lingoflow.lingoflowbackend.model.dto.ArticleGenerateRequest;
import com.lingoflow.lingoflowbackend.model.entity.Article;
import com.lingoflow.lingoflowbackend.model.vo.ArticleVO;

public interface ArticleService extends IService<Article> {

    /**
     * 调用大模型重写文章，提取生词，并落库保存
     * @param userId 当前登录用户的 ID
     * @param request 包含原文和目标难度
     * @return 包含重写后文章信息的视图对象
     */
    ArticleVO generateAndSaveArticle(Long userId, ArticleGenerateRequest request);
}