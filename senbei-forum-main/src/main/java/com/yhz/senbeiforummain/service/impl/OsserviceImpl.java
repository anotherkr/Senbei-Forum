package com.yhz.senbeiforummain.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.yhz.commonutil.common.ErrorCode;

import com.yhz.senbeiforummain.domain.third.OssRequestParam;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.IOssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author yanhuanzhan
 * @date 2022/11/21 - 12:44
 */
@Slf4j
@Service
public class OsserviceImpl implements IOssService {
        @Resource
        private OSS ossClient;
        @Value("${spring.cloud.alicloud.oss.endpoint}")
        private String endpoint;
        @Value("${spring.cloud.alicloud.oss.bucket}")
        private String bucket;
        @Value("${spring.cloud.alicloud.access-key}")
        private String accessId;
        @Override
        public OssRequestParam policy() throws BusinessException {
            // 填写Host名称，格式为https://bucketname.endpoint。
            String host = "https://"+bucket+"."+endpoint;
            String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
            String dir = format+"/";
            OssRequestParam ossRequestParam;
            try {
                long expireTime = 300;
                long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
                Date expiration = new Date(expireEndTime);
                // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
                PolicyConditions policyConds = new PolicyConditions();
                policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
                policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

                String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
                byte[] binaryData = postPolicy.getBytes("utf-8");
                String encodedPolicy = BinaryUtil.toBase64String(binaryData);
                String postSignature = ossClient.calculatePostSignature(postPolicy);
                ossRequestParam = new OssRequestParam();
                ossRequestParam.setAccessid(accessId);
                ossRequestParam.setPolicy(encodedPolicy);
                ossRequestParam.setSignature(postSignature);
                ossRequestParam.setDir(dir);
                ossRequestParam.setHost(host);
                ossRequestParam.setExpire(String.valueOf(expireEndTime / 1000));
            } catch (Exception e) {
                log.error("exception:{}",e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            } finally {
                ossClient.shutdown();
            }
            return ossRequestParam;
        }


}
