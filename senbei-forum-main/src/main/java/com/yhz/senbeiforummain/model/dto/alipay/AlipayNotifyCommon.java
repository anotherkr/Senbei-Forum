package com.yhz.senbeiforummain.model.dto.alipay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author 吉良吉影
 */
@Data
public class AlipayNotifyCommon {
    @JSONField(name = "notify_time",format = "yyyy-MM-dd HH:mm:ss")
    private Date notifyTime;
    @JSONField(name = "notify_type")
    private String notifyType;
    @JSONField(name = "notify_id")
    private String notifyId;

    private String charset;

    private String version;
    @JSONField(name = "sign_type")
    private String signType;
    private String sign;
    @JSONField(name = "auth_app_id")
    private String authAppId;
}
