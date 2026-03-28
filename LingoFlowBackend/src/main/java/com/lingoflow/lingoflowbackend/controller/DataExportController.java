package com.lingoflow.lingoflowbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingoflow.lingoflowbackend.common.Result;
import com.lingoflow.lingoflowbackend.model.dto.LingoDataDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/import")
    public Result<Boolean> importUserData(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(400, "上传的文件为空！");
        }

        try {
            // 1. 将接收到的 JSON 文件流读取为后端的 DTO 对象
            ObjectMapper objectMapper = new ObjectMapper();
            // LingoDataDTO 是你之前写导出功能时用来包裹所有数据的实体类
            LingoDataDTO importData = objectMapper.readValue(file.getInputStream(), LingoDataDTO.class);

            // 2. 这里写你的业务逻辑：
            // 比如清空当前用户的数据，然后把 importData 里面的 List<Article> 和 List<Vocabulary>
            // 重新通过 articleService.saveBatch() 塞进数据库。
            // ... 你的导入逻辑 ...

            return Result.success(true);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "JSON解析失败或导入失败: " + e.getMessage());
        }
    }
}