package com.yhz.senbeiforummain.domain.oos;

import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2022/11/21 - 13:08
 */
@Data
public class OssRequestParam {

    private String accessid;
    private String policy;
    private String signature;
    private String dir;
    private String host;
    private String expire;
}
