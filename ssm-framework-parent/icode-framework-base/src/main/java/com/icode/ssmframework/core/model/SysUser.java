package com.icode.ssmframework.core.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;

public class SysUser  extends Model<SysUser>{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String password;
	private String nickName;//昵称
	private Date registTime;//账号注册时间
	private Date validEndTime;//账号有效截止日期accountNonExpired
	private Date lastModifyPwdTime;//上次密码修改，可判断账户密码是否过期（如90天修改一次密码）credentialsNonExpired
	private Date lastLoginTime;//上次登陆系统时间
	private Boolean locked;//账号是否被锁定
	private Boolean enabled;//账号是否禁用
	private Date insertTimeForHis;
	private Date updateTimeForHis;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Date getRegistTime() {
		return registTime;
	}
	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}
	public Date getValidEndTime() {
		return validEndTime;
	}
	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}
	public Date getLastModifyPwdTime() {
		return lastModifyPwdTime;
	}
	public void setLastModifyPwdTime(Date lastModifyPwdTime) {
		this.lastModifyPwdTime = lastModifyPwdTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Boolean getLocked() {
		return locked;
	}
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Date getInsertTimeForHis() {
		return insertTimeForHis;
	}
	public void setInsertTimeForHis(Date insertTimeForHis) {
		this.insertTimeForHis = insertTimeForHis;
	}
	public Date getUpdateTimeForHis() {
		return updateTimeForHis;
	}
	public void setUpdateTimeForHis(Date updateTimeForHis) {
		this.updateTimeForHis = updateTimeForHis;
	}
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", username=" + username + ", password=" + password + ", insertTimeForHis="
				+ insertTimeForHis + ", updateTimeForHis=" + updateTimeForHis + "]";
	}
	@Override
	protected Serializable pkVal() {
		return id;
	}
}
