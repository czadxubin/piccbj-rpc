package com.xbz.ssmframework.login.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.jetty.util.security.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.xbz.ssmframework.core.SysGrantedAuthority;
import com.xbz.ssmframework.model.SysRole;
import com.xbz.ssmframework.model.SysUser;
import com.xbz.ssmframework.user.controller.SysUserMapper;

@Service
public class JdbcUserDetailService implements UserDetailsService{
	@Autowired
	private SysUserMapper sysUserMapper;
	private final List<SysGrantedAuthority> DEFAULT_NONE_SYSGRANTEDAUTHORITY = new ArrayList<SysGrantedAuthority>();
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails ud = null;
		SysUser queryUser = new SysUser();
		queryUser.setUsername(username);
		SysUser sysUser = sysUserMapper.selectOne(queryUser);
		if(sysUser!=null) {
			String password = sysUser.getPassword();
			List<SysGrantedAuthority> authorities = null;
			List<SysRole> roles = sysUserMapper.selectRolesByUsername(username);
			if(roles!=null&&roles.size()>0) {
				authorities = new ArrayList<SysGrantedAuthority>(roles.size());
				for (SysRole sysRole : roles) {
					authorities.add(new SysGrantedAuthority(sysRole));
				}
			}
			//账号是否可用
			boolean enabled = sysUser.getEnabled();
			//账号是否未过期，与当前时间与有效期比较
			boolean accountNonExpired = true;
			Date now = new Date();
			Date validEndTime = sysUser.getValidEndTime();
			if(validEndTime!=null) {
				accountNonExpired = now.compareTo(validEndTime)<=0;
			}
			//证书是否过未过期，密码上次修改时间+90天>当前时间
			boolean credentitialNonExpired = true;
			boolean accountNonLocked = !sysUser.getLocked();
			ud = new User(username,password,enabled,accountNonExpired,credentitialNonExpired,accountNonLocked,authorities);
		}else {
			throw new UsernameNotFoundException("用户名不存在");
		}
		return ud;
	}

}
