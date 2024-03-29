package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.model.dto.user.UserReplyRequest;
import com.yhz.senbeiforummain.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import com.yhz.senbeiforummain.model.vo.UserReplyVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author 吉良吉影
* @description 针对表【user】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface IUserService extends IService<User> {

    User getUserByUserName(String username);

    UserInfoVo getUserInfoByToken(HttpServletRequest request);

    /**
     * 获取用户评论信息
     * @param userReplyRequest
     * @return
     */
    IPage<UserReplyVo> getUserReplyVoPage(UserReplyRequest userReplyRequest);
}
