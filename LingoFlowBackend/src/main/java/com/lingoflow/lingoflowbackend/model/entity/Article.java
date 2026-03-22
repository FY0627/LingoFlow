package com.lingoflow.lingoflowbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("lingo_article")
public class Article {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String originalText;

    private String adaptedText;

    private String targetLanguage;

    private String difficultyLevel;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}