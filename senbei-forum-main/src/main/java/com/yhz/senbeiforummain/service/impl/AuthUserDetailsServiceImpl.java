package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.mapper.UserMapper;
import com.yhz.senbeiforummain.security.domain.AuthUser;
import com.yhz.senbeiforummain.service.IRoleService;
import com.yhz.senbeiforummain.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 要实现UserDetailsService接口，这个接口是security提供的
 * @author 吉良吉影
 */
@Service(value = "userDetailsService")
public class AuthUserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(AuthUserDetailsServiceImpl.class);

    @Resource
    private IUserService userService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private IRoleService roleService;

    /**
     * 通过账号查找用户、角色的信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUserName(username);
        if (user == null) {
            //用户名不存在
            throw new BusinessException(ErrorCode.NULL_ERROR);
        } else {
            //查找角色，实际应该查询权限，但我数据库没有设计所以就查角色就好了
            List<String> roleNameList = roleService.getRolesByUserName(username);
            if (CollUtil.isEmpty(roleNameList)) {
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            List<SimpleGrantedAuthority> authorities = roleNameList.stream().map(roleName -> new SimpleGrantedAuthority(roleName)).collect(Collectors.toList());
            return new AuthUser(user, authorities);
        }
    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDetails.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (ObjectUtil.isEmpty(user)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        user.setPassword(newPassword);
        int update = userMapper.updateById(user);
        if (update == 1) {
            ((User) userDetails).setPassword(newPassword);
        }
        return userDetails;
    }
}

