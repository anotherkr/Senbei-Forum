package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.senbeiforummain.domain.User;
import com.yhz.senbeiforummain.service.IUserService;
import com.yhz.senbeiforummain.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 吉良吉影
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2022-11-13 16:08:53
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {

}




