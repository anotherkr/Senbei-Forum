package com.yhz.senbeiforummain;

import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.security.domain.AuthUser;
import com.yhz.senbeiforummain.service.IModuleService;
import com.yhz.senbeiforummain.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class SenbeiForumMainApplicationTests {
    @Resource
    IModuleService moduleService;
    @Resource
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        for (int i = 1; i < 10; i++) {
            Module module = new Module();
            module.setName("模块" + i)
                    .setUserId(1L)
                    .setTopicNum(100L)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date())
                    .setCreateBy("先辈")
                    .setUpdateBy("先辈");
            if (i % 5 == 1) {
                module.setImgUrl("https://mail-yhz.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E5%91%86.jpg");
            } else if (i % 5 == 2) {
                module.setImgUrl("https://mail-yhz.oss-cn-guangzhou.aliyuncs.com/%E5%9B%BE%E7%89%87/OIP-C.jpg");
            }else if(i % 5 == 3){
                module.setImgUrl("https://mail-yhz.oss-cn-guangzhou.aliyuncs.com/%E5%9B%BE%E7%89%87/th.jpg");
            }else {
                module.setImgUrl("https://mail-yhz.oss-cn-guangzhou.aliyuncs.com/%E5%9B%BE%E7%89%87/v2.jpg");
            }

            moduleService.save(module);
        }

    }
    @Test
    public void test01(){


        // 要加密的数据（如数据库的用户名或密码）
        String username = encrypt("1");
        String password = encrypt("1");
        log.info("username:{}",username);
        System.out.println("加密：password:" + password);
    }
    /**
     * 加密
     * @param plaintext 明文密码     * @return
     */
    public static String encrypt(String plaintext) {
        //加密工具
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        //加密配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        // 算法类型
        config.setAlgorithm("PBEWithMD5AndDES");
        //生成秘钥的公钥
        config.setPassword("yhz");
        //应用配置
        encryptor.setConfig(config);
        //加密
        String ciphertext = encryptor.encrypt(plaintext);
        return ciphertext;
    }

    @Resource
    RedisCache redisCache;
    @Test
    public void test02() {
        User user = new User();
        user.setUsername("111");
        user.setPassword("222");
        redisTemplate.opsForValue().set("user",user);
        User newUser = (User) redisTemplate.opsForValue().get("user");
        System.out.println(newUser.toString());
    }

}
