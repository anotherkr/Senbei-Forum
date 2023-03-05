package com.yhz.senbeiforummain.config.pay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author yanhuanzhan
 * @date 2022/5/18 - 23:41
 */
@Configuration
@ConfigurationProperties(prefix = "alipay")
@PropertySource("classpath:alipay.properties")
@Data
public class AlipayConfig {
    private String appid;
    /**
     * 商户PID,卖家支付宝账号id
     */
    private String sellerId;
    /**
     * 商户私钥,PKCS8格式RSA2私钥
     */
    private String merchantPrivateKey;
    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;
    /**
     * 支付宝网关地址(沙箱版)
     */
    private String gatewayUrl;
    /**
     * 接口内容加密秘钥，对称秘钥
     */
    private String contentKey;

    private String returnUrl;

    private String notifyDomain;
}
