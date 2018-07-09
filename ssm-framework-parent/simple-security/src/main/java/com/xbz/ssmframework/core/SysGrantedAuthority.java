package com.xbz.ssmframework.core;

import org.springframework.security.core.GrantedAuthority;

import com.xbz.ssmframework.model.SysRole;

/**
 * 自定义授权pojo
 * @author 许宝众
 *
 */
public class SysGrantedAuthority implements GrantedAuthority{
	private static final long serialVersionUID = 1L;
	private SysRole sysRole;
	private String roleName;
	public SysGrantedAuthority(SysRole sysRole) {
		this.sysRole = sysRole;
		this.roleName = sysRole.getRoleName();
	}
	@Override
	public String getAuthority() {
		return roleName;
	}
	public SysRole getSysRole() {
		return sysRole;
	}
	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
