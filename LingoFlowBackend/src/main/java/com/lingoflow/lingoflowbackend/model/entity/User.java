package com.lingoflow.lingoflowbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("lingo_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String passwordHash;

    private String targetLanguage;

    private String currentLevel;

    /**
     * 是否逻辑删除：1是，0否
     * @TableLogic 注解告诉 MyBatis-Plus 这是一个逻辑删除字段
     * 调用 deleteById 时，底层会自动转为 update is_deleted = 1
     */
    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}