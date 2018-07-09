package com.xbz.ssmframework.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;

public class SysRole extends Model<SysRole>{
	private static final long serialVersionUID = 1L;
	private Long id;// int primary key auto_increment,
	private String roleCode;//角色代码
	private String roleName;//角色名称
	private String parentCode;//父亲角色代码，顶级与自身角色代码相同
	private Boolean enabled;//当前角色是否可用
	private Date insertTimeForHis;//insert_time_for_his timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	private Date updateTimeForHis;//update_time_for_his timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
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
	protected Serializable pkVal() {
		
		return null;
	}

}
