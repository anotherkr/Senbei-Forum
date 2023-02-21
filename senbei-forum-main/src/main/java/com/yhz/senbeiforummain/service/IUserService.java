package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吉良吉影
* @description 针对表【user】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface IUserService extends IService<User> {

    User getUserByUserName(String username);

}
