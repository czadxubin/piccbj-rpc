package com.xbz.ssmframework.security.oauth2.provider.code;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAlias;
/**
 * 为SSO单点接入提供授权码服务
 * <br>
 * 生成code以及消费code
 * @author 许宝众
 *
 */
@Service
@SuppressWarnings("rawtypes")
public class ICodeAuthorizationCodeServices implements AuthorizationCodeServices,InitializingBean{
	private static Logger logger = LoggerFactory.getLogger(ICodeAuthorizationCodeServices.class);
	private static final String DEFAULT_AUTHORIZATION_CODE_PREFFIX = "ICODE_AUHOR_CODE:";
	/**code保存至redis时，加一个前缀**/
	private String preffix = DEFAULT_AUTHORIZATION_CODE_PREFFIX;
	/**code生成器，方便重写**/
	private RandomValueStringGenerator codeGenerator = new RandomValueStringGenerator();
	/**授权Code有效时间，默认5分钟**/
	private Long authorizationCodeValiditySeconds = 5*60L;
	@Autowired
	@Qualifier("oauth2RedisTemplate")
	private RedisTemplate oauth2RedisTemplate;
	@Override
	public String createAuthorizationCode(OAuth2Authentication authentication) {
		String code = null;
		do{
			code = codeGenerator.generate();
		}
		while(!store(code,authentication));
		return code;
	}

	@Override
	public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
		return this.remove(code);
	}
	
	/**
	 * 保存code,确保可以通过code得到授权请求信息
	 * @param code
	 * @param authentication 
	 */
	@SuppressWarnings("unchecked")
	protected boolean store(String code, OAuth2Authentication authentication) {
		return (boolean) oauth2RedisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] codeKey = null;
				try {
					codeKey = (preffix+code).getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				Boolean setNX = connection.setNX(codeKey, SerializationUtils.serialize(authentication));
				if(setNX) {
					connection.expire(codeKey, authorizationCodeValiditySeconds);
					logger.debug("[授权code存储成功]-[授权码：]-[序列化OAuth2Authentication信息：]",code,JSON.toJSONString(authentication));
					return true;
				}
				
//				StringRedisConnection conn = (StringRedisConnection)connection;
//				String authenticationJson = JSON.toJSONString(authentication);
//				Boolean setNX = conn.setNX(codeKey, authenticationJson);
//				if(setNX) {
//					conn.expire(codeKey, authorizationCodeValiditySeconds);
//					logger.debug("[授权code存储成功]-[授权码：]-[序列化OAuth2Authentication信息：]",code,authenticationJson);
//					return true;
//				}
				return false;
			}
		});
	}
	
	/**
	 * 使用后移除code
	 * @param code
	 */
	@SuppressWarnings("unchecked")
	protected OAuth2Authentication remove(String code) {
		return (OAuth2Authentication) oauth2RedisTemplate.execute(new RedisCallback<OAuth2Authentication>() {
			@Override
			public OAuth2Authentication doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] codeKey = null;
				try {
					codeKey = (preffix+code).getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				byte[] content = connection.get(codeKey);
				if(content!=null) {
					connection.del(codeKey);
					OAuth2Authentication oAuth2Authentication = SerializationUtils.deserialize(content);
					logger.debug("[授权code取出成功]-[授权码：]-[反序列化OAuth2Authentication信息：]",code,JSON.toJSONString(oAuth2Authentication));
					return oAuth2Authentication;
				}
				
				
//				StringRedisConnection conn = (StringRedisConnection)connection;
//				String content = conn.get(codeKey);
//				if(content!=null) {
//					conn.del(codeKey);
//					OAuth2Authentication oAuth2Authentication = JSON.parseObject(content, OAuth2Authentication.class);
//					logger.debug("[授权code取出成功]-[授权码：]-[反序列化OAuth2Authentication信息：]",code,JSON.toJSONString(oAuth2Authentication));
//					return oAuth2Authentication;
//				}
				return null;
			}
		});
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(oauth2RedisTemplate,"oauth2RedisTemplate must not be null");
	}

	public String getPreffix() {
		return preffix;
	}

	public void setPreffix(String preffix) {
		this.preffix = preffix;
	}

	public RandomValueStringGenerator getCodeGenerator() {
		return codeGenerator;
	}

	public void setCodeGenerator(RandomValueStringGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}

	public Long getAuthorizationCodeValiditySeconds() {
		return authorizationCodeValiditySeconds;
	}

	public void setAuthorizationCodeValiditySeconds(Long authorizationCodeValiditySeconds) {
		this.authorizationCodeValiditySeconds = authorizationCodeValiditySeconds;
	}

	public RedisTemplate getOauth2RedisTemplate() {
		return oauth2RedisTemplate;
	}

	public void setOauth2RedisTemplate(RedisTemplate oauth2RedisTemplate) {
		this.oauth2RedisTemplate = oauth2RedisTemplate;
	}
}
