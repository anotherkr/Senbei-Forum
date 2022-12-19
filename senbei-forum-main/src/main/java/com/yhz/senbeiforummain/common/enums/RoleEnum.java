package com.yhz.senbeiforummain.common.enums;

/**
 * @author yanhuanzhan
 * @date 2022/12/5 - 16:22
 */
public enum RoleEnum {



    ROLE_USER(1L, "role_user", "普通用户"),
    ROLE_VIP(2L, "role_vip", "会员"),
    ROLE_VVIP(3L,"role_vvip","超级会员"),
    ROLE_ADMIN(4L,"role_admin","超级管理员");
    private final Long id;

    /**
     * 状态码信息
     */
    private final String roleName;

    /**
     * 状态码描述（详情）
     */
    private final String roleDescription;

    RoleEnum(Long id, String roleName, String roleDescription) {
        this.id = id;
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }
}
