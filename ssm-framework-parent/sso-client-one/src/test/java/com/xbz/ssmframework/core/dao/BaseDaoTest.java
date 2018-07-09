package com.xbz.ssmframework.core.dao;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.xbz.ssmframework.user.controller.SysUserMapper;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试    
@ContextConfiguration(locations={"classpath*:/spring/app-mvc.xml","/spring/application-*.xml"})
public class BaseDaoTest {
	@Autowired
	private SysUserMapper sysUserMapper;
	@BeforeClass
	public static void prepareJdniDataSource() {
		try {
			ComboPooledDataSource ds = new ComboPooledDataSource();
			String user = "root";
			String password="root";
			String jdbcUrl = "jdbc:mysql://localhost:3306/ssm_framwork?useUnicode=true&characterEncoding=UTF-8";
			String driverClass="com.mysql.jdbc.Driver";
			ds.setUser(user);
			ds.setPassword(password);
			ds.setJdbcUrl(jdbcUrl);
			ds.setDriverClass(driverClass);
			ds.setInitialPoolSize(2);
			ds.setMaxPoolSize(5);
			SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();  
			builder.bind("java:comp/env/dataSource", ds);  
			builder.activate();  
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testSelectOneById() {
	}
}
