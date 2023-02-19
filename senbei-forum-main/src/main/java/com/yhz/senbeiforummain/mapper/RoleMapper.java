package com.yhz.senbeiforummain.mapper;

import com.yhz.senbeiforummain.model.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【role(角色表)】的数据库操作Mapper
* @createDate 2022-12-04 23:52:46
* @Entity com.yhz.senbeiforummain.domain.Role
*/
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户名获取角色
     * @param username
     * @return
     */
    List<Role> getRolesByUserName(@Param("username")String username);

    /**
     * 根据用户id关联角色
     * @param roleId
     * @param userId
     * @return
     */
    int attachRoleByUserId(@Param("roleId") Long roleId,@Param("userId") Long userId);
}




