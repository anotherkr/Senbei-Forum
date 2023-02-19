package com.yhz.senbeiforummain.security.domain;

import com.yhz.senbeiforummain.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @Author LZDWTL
 * @Date 2021-12-15 23:35
 * @ClassName AuthUser
 * @Description 实现UserDetails，仿写User的原因是 防止User类名和自己创建的实体类 User 重合（虽然我这里创建的不是User而是TUser）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser implements UserDetails {

    private User user;

//    @JSONField(serialize = false)
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 账户是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未被锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
