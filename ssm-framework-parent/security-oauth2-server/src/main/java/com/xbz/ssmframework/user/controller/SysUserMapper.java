package com.xbz.ssmframework.user.controller;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xbz.ssmframework.model.SysRole;
import com.xbz.ssmframework.model.SysUser;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser>{
	@Select("select r.* from sys_role r,sys_user u,sys_role_user ru where r.id = ru.role_id and u.id = ru.user_id and u.username = #{username} and r.enabled = true")
	public List<SysRole> selectRolesByUsername(@Param("username")String username);
}
