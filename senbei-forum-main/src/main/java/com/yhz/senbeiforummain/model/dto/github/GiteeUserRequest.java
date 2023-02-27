package com.yhz.senbeiforummain.model.dto.github;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yanhuanzhan
 * @date 2022/12/16 - 15:03
 */
@Data
public class GiteeUserRequest implements Serializable {
    /**
     * 用户昵称
     */
    private String name;

    private Long id;
    /**
     * 用户头像
     */
    @JSONField(name = "avatar_url")
    private String avatarUrl;


}
