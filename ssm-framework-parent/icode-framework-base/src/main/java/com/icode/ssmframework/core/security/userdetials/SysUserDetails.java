package com.icode.ssmframework.core.security.userdetials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.icode.ssmframework.core.model.SysUser;
import com.icode.ssmframework.core.security.SysGrantedAuthority;

public class SysUserDetails implements UserDetails{
	private static final long serialVersionUID = 1L;
	/**
	 * 账号密码过期天数
	 */
	private static final int PWD_EXPIRED_DAYS = 90;
	private static final List<SysGrantedAuthority> NULL_GrantedAuthority = new ArrayList<SysGrantedAuthority>();
	private List<SysGrantedAuthority> authorities;
	private SysUser user;
	public SysUserDetails() {}
	public SysUserDetails(SysUser user) {
		this(user,NULL_GrantedAuthority);
	}
	public SysUserDetails(SysUser user, List<SysGrantedAuthority> authorities) {
		this.user = user;
		this.authorities = authorities;
	}
	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public void setAuthorities(List<SysGrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	/***
	 * 账号未过期
	 */
	@Override
	public boolean isAccountNonExpired() {
		Date validEndTime = user.getValidEndTime();
		Date now = new Date();
		return now.compareTo(validEndTime)<=0;
	}
	
	/**
	 * 账号未锁定
	 */
	@Override
	public boolean isAccountNonLocked() {
		return !this.user.getLocked();
	}

	/**
	 * 密码（凭证）未过期
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		Date lastModifyPwdTime = user.getLastModifyPwdTime();
		Date now = new Date();
		return DateUtils.addDays(lastModifyPwdTime, PWD_EXPIRED_DAYS).compareTo(now)>=0;
	}

	@Override
	public boolean isEnabled() {
		return this.user.getEnabled();
	}
}
