package com.yhz.senbeiforummain.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 处理新增和更新的基础数据填充，配合BaseEntity和MyBatisPlusConfig使用
 * @author yanhuanzhan
 * @date 2022/11/28 - 15:20
 */
@Component
public class MetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(), metaObject);
        //this.setFieldValByName("createBy", userEntity.getLoginName(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        //this.setFieldValByName("updateBy", userEntity.getLoginName(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        //this.setFieldValByName("updateBy", userEntity.getLoginName(), metaObject);
    }
}
