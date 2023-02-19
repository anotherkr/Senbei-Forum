package com.yhz.senbeiforummain.model.dto.github;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yanhuanzhan
 * @date 2022/12/16 - 4:17
 */
@Data
public class GithubTokenRequest implements Serializable {
    private static final long serialVersionUID = -3452025704003314026L;
    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "token_type")
    private String tokenType;

    private String scope;
}
