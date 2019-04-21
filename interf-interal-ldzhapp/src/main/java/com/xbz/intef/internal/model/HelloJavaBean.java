package com.xbz.intef.internal.model;

/**
 * 用于演示内部接口业务方法入参封装为javaBean的案例
 * @author è???°????
 *
 */
public class HelloJavaBean {
	private String username;
	private Long id;
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
}
