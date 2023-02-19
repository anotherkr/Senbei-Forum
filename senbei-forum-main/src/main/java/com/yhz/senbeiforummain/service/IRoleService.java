package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.model.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2022-12-04 23:52:46
*/
public interface IRoleService extends IService<Role> {
    /**
     * 根据用户名查询角色
     * @param username
     * @return
     */
    List<String> getRolesByUserName(String username);

    /**
     * 根据用户id关联角色
     *
     * @param roleId
     * @param userId
     * @return
     */
    boolean attachRoleByUserId(Long roleId, Long userId);
}
