package com.yhz.senbeiforummain.mapper;

import com.yhz.senbeiforummain.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

/**
* @author 吉良吉影
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-11-13 16:08:53
* @Entity com.yhz.senbeiforummain.domain.User
*/

public interface UserMapper extends BaseMapper<User> {
     /**
      * 根据用户id查询userInfo
      * @param userId
      * @return
      */
     UserInfoVo getUserInfoVoById(@Param("userId")Long userId);
}




