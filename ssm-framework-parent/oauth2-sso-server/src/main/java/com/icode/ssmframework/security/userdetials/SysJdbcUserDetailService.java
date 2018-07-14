package com.icode.ssmframework.security.userdetials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.icode.ssmframework.core.model.SysRole;
import com.icode.ssmframework.core.model.SysUser;
import com.icode.ssmframework.core.security.SysGrantedAuthority;
import com.icode.ssmframework.core.security.userdetials.SysUserDetails;
import com.icode.ssmframework.sso.mapper.SysUserMapper;

@Service
public class SysJdbcUserDetailService implements UserDetailsService{
	@Autowired
	private SysUserMapper sysUserMapper;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails ud = null;
		SysUser queryUser = new SysUser();
		queryUser.setUsername(username);
		SysUser sysUser = sysUserMapper.selectOne(queryUser);
		if(sysUser!=null) {
			List<SysGrantedAuthority> authorities = null;
			List<SysRole> roles = sysUserMapper.selectRolesByUsername(username);
			if(roles!=null&&roles.size()>0) {
				authorities = new ArrayList<SysGrantedAuthority>(roles.size());
				for (SysRole sysRole : roles) {
					authorities.add(new SysGrantedAuthority(sysRole));
				}
			}
//			ud = new User(username,password,enabled,accountNonExpired,credentitialNonExpired,accountNonLocked,authorities);
			ud = new SysUserDetails(sysUser, authorities==null?Collections.emptyList():authorities);
		}else {
			throw new UsernameNotFoundException("用户名不存在");
		}
		return ud;
	}

}
