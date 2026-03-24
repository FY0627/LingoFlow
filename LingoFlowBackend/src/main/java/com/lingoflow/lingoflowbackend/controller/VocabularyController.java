package com.lingoflow.lingoflowbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingoflow.lingoflowbackend.common.Result;
import com.lingoflow.lingoflowbackend.model.dto.VocabularyAddRequest;
import com.lingoflow.lingoflowbackend.model.vo.VocabularyVO;
import com.lingoflow.lingoflowbackend.service.VocabularyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vocabulary")
public class VocabularyController {

    @Autowired
    private VocabularyService vocabularyService;

    /**
     * 手动添加生词
     */
    @PostMapping("/add")
    public Result<Boolean> addVocabulary(@RequestBody VocabularyAddRequest request, HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getAttribute("userId");
        Boolean success = vocabularyService.addVocabulary(userId, request);
        return Result.success(success);
    }

    /**
     * 分页获取生词列表（支持筛选）
     */
    @GetMapping("/list")
    public Result<Page<VocabularyVO>> getVocabularyList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer mastered,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Page<VocabularyVO> voPage = vocabularyService.getVocabularyList(userId, page, size, mastered);
        return Result.success(voPage);
    }

    /**
     * 标记生词为已掌握
     */
    @PutMapping("/master/{id}")
    public Result<Boolean> markAsMastered(@PathVariable("id") Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Boolean success = vocabularyService.markAsMastered(userId, id);
        return Result.success(success);
    }
}