CREATE DATABASE IF NOT EXISTS ssm_framwork DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
use ssm_framwork;
-- 用户表
drop table if exists sys_user;
create Table sys_user(
	id int primary key auto_increment,
	username varchar(50) not null unique,
	password varchar(128),
	nick_name varchar(100),			-- 昵称
	regist_time datetime,			-- 账号注册时间
	valid_end_time date,			-- 账号有效截止日期accountNonExpired
	last_modify_pwd_time datetime,	-- 上次密码修改，可判断账户密码是否过期（如90天修改一次密码）credentialsNonExpired
	last_login_time datetime,		-- 上次登陆系统时间
	locked tinyint(1) default false,				-- 账号是否被锁定
	enabled tinyint(1) default true,				-- 账号是否禁用
	insert_time_for_his timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	update_time_for_his timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色表
drop table if exists sys_role;
create Table sys_role(
	id int primary key auto_increment,
	role_code varchar(100) not null unique,		-- 角色代码
	role_name varchar(200) not null,			-- 角色名称
	parent_code varchar(100) not null,			-- 父亲角色代码，顶级与自身角色代码相同
	enabled tinyint(1) default true,						-- 当前角色是否可用
	insert_time_for_his timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	update_time_for_his timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色用户中间表
drop table if exists sys_role_user;
create Table sys_role_user(
	role_id int,
	user_id int,
	primary key (role_id,user_id)
);


-- 添加测试用户、角色
insert into sys_user(username,password,nick_name,regist_time,valid_end_time,last_modify_pwd_time) values('czadxubin','$2a$10$.p0ZklYxzFXhNd6zP8whUeBfMDVAE.wjFIkRZjwEPvFqQszNXYcJy','许宝众',CURRENT_TIMESTAMP,'2020-12-01',CURRENT_TIMESTAMP);
insert into sys_role(role_code,role_name,parent_code) values('ROLE_ADMIN','系统管理员','ROLE_ADMIN');
insert into sys_role_user(role_id,user_id) values(1,1);