package com.yhz.senbeiforummain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName module_topic_content
 */
@TableName(value ="module_topic_content")
@Data
public class ModuleTopicContent implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 主贴id
     */
    private Long topicId;

    /**
     * 主贴内容
     */
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}