package com.lingoflow.lingoflowbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingoflow.lingoflowbackend.model.entity.Article;
import com.lingoflow.lingoflowbackend.model.entity.Vocabulary;
import com.lingoflow.lingoflowbackend.service.ArticleService;
import com.lingoflow.lingoflowbackend.service.VocabularyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataExportController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private VocabularyService vocabularyService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 一键导出用户的全部阅读历史和生词本为 JSON 文件
     */
    @GetMapping("/export")
    public void exportUserData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long userId = (Long) request.getAttribute("userId");

        // 1. 一次性查出该用户所有的文章记录
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getUserId, userId).orderByDesc(Article::getCreateTime);
        List<Article> articles = articleService.list(articleWrapper);

        // 2. 一次性查出该用户所有的生词记录
        LambdaQueryWrapper<Vocabulary> vocabWrapper = new LambdaQueryWrapper<>();
        vocabWrapper.eq(Vocabulary::getUserId, userId).orderByDesc(Vocabulary::getCreateTime);
        List<Vocabulary> vocabularies = vocabularyService.list(vocabWrapper);

        // 3. 将数据打包进一个 Map 结构中
        Map<String, Object> exportData = new HashMap<>();
        exportData.put("exportTime", LocalDateTime.now().toString());
        exportData.put("totalArticles", articles.size());
        exportData.put("totalVocabularies", vocabularies.size());
        exportData.put("articles", articles);
        exportData.put("vocabularies", vocabularies);

        // 4. 【核心】设置 HTTP 响应头，告诉浏览器这是一个需要下载的附件
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "LingoFlow_Data_" + timestamp + ".json";

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        // Content-Disposition: attachment 是触发浏览器下载框的关键指令
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        // 5. 将组装好的对象转成 JSON 字符串，并直接写入响应流
        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(exportData);
        response.getWriter().write(jsonString);
    }
}