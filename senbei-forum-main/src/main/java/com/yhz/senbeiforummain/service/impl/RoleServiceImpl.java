package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.domain.Role;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.IRoleService;
import com.yhz.senbeiforummain.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 吉良吉影
 * @description 针对表【role(角色表)】的数据库操作Service实现
 * @createDate 2022-12-04 23:52:46
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements IRoleService {

    @Override
    public List<String> getRolesByUserName(String username) {
        List<Role> roleList = this.baseMapper.getRolesByUserName(username);
        List<String> roleNameList = roleList.stream().map(role -> role.getRoleName()).collect(Collectors.toList());
        if (CollUtil.isEmpty(roleList)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return roleNameList;
    }

    @Override
    public boolean attachRoleByUserId(Long roleId, Long userId) {
        int result = 0;
        try{
            result = this.baseMapper.attachRoleByUserId(roleId, userId);
        }catch (Exception e){
            log.error("method[attachRoleByUserId] fail,msg:{}",e);
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }

        return result == 1;
    }
}




