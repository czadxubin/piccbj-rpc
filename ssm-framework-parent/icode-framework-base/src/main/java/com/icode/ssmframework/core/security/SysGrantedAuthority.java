package com.icode.ssmframework.core.security;

import org.springframework.security.core.GrantedAuthority;

import com.icode.ssmframework.core.model.SysRole;

/**
 * 自定义授权pojo
 * @author 许宝众
 *
 */
public class SysGrantedAuthority implements GrantedAuthority{
	private static final long serialVersionUID = 1L;
	private SysRole sysRole;
	private String roleName;
	public SysGrantedAuthority(){}
	public SysGrantedAuthority(SysRole sysRole) {
		this.sysRole = sysRole;
		this.roleName = sysRole.getRoleCode();
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
