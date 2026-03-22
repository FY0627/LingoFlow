package com.lingoflow.lingoflowbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("lingo_vocabulary")
public class Vocabulary {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long articleId;

    private String word;

    private String translation;

    private String contextSentence;

    /**
     * 是否已掌握：1是，0否
     */
    private Integer mastered;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}