package com.yhz.senbeiforummain.domain.github;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yanhuanzhan
 * @date 2022/12/16 - 15:03
 */
@Data
public class GithubUser implements Serializable {
    /**
     * 用户昵称
     */
    private String login;

    private Long id;
    @JSONField(name = "node_id")
    private String nodeId;
    /**
     * 用户头像
     */
    @JSONField(name = "avatar_url")
    private String avatarUrl;
}
