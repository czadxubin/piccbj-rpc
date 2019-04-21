package com.xbz.intef.internal.server.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xbz.intef.internal.HandleStatus;
import com.xbz.intef.internal.model.InternalApiReq;
import com.xbz.intef.internal.model.InternalApiReqHead;
import com.xbz.intef.internal.server.exception.InternalApiException;
import com.xbz.intef.internal.util.InternalApiUtils;
import com.xbz.intef.internal.util.NetworkUtils;

@Component
public class HandlerChain implements ApplicationContextAware{
	private static Logger logger = LoggerFactory.getLogger(HandlerChain.class);
	private ApplicationContext act;
	private List<AbstractInHandler> inHanlders;
	/**服务器IP**/
	private String serverIp;
	/**服务器端口**/
	private Integer port;

	public HandlerChain() {
		this.inHanlders = new ArrayList<AbstractInHandler>();
		AbstractInHandler authHandler = new AuthencationHandler();
		this.inHanlders.add(authHandler);
	}
	public String invokeService(HttpServletRequest request,HttpServletResponse response) {
		if(this.port==null) {
			this.port = request.getLocalPort();
		}
		Throwable ex = null;
		String reqData = null;
		String resBody = null;
		String reqId = null;
		//报文中的请求时间
		Date reqTime = null;
		//实际接收到请求时间
		Date recvTime = new Date();
		InternalApiContext ctx = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String oneLine = null;
			StringBuffer reqSb = new StringBuffer();
			while((oneLine = br.readLine())!=null) {
				reqSb.append(oneLine+"\n");
			}
			reqData = reqSb.toString();
			if(StringUtils.isNotBlank(reqData)) {
				logger.debug("[内部接口API]-[准备处理请求]-[请求数据："+reqData+"]");
				//序列化请求参数
				InternalApiReq req = JSON.parseObject(reqData,InternalApiReq.class);
				if(req!=null) {
					InternalApiReqHead reqHead = req.getReqHead();
//						string	Y	请求编号
					reqId = reqHead.getReqId();
//						string	Y	请求时间 日期格式： yyyy-MM-dd HH:mm:ss.SSS
					reqTime = reqHead.getReqTime();
//						string	Y	调用端应用ID（需申请）
					String appId = reqHead.getAppId();
//						string 	Y	分布式服务器IP地址
					String servIp = reqHead.getServIp();
//						string 	Y	分布式服务器端口
					Integer servPort = reqHead.getServPort();
//						string	Y	服务名称
					String serviceName = reqHead.getServiceName();
//						string	Y	服务方法
					String serviceMethod = reqHead.getServiceMethod();
//						string	N	服务版本号
					Integer serviceVerison = reqHead.getServiceVerison();
					if(StringUtils.isNotBlank(reqId)
							&&reqTime!=null
							&&StringUtils.isNotBlank(appId)
							&&StringUtils.isNotBlank(servIp)
							&&servPort!=null
							&&StringUtils.isNotBlank(serviceName)
							&&StringUtils.isNotBlank(serviceMethod)
							//&&StringUtils.isNotBlank(serviceVerison) 允许版本号为空
							) {
						//如果serviceVersion不为空，则ServiceMethod为带版本方法名称
						if(serviceVerison!=null) {
							serviceMethod += "V"+serviceVerison;
						}
						String serviceData = req.getReqBody();
						//进入执行链进行请求处理
						ctx = new InternalApiContext();
						ctx.setReq(req);
						ctx.setRequest(request);
						ctx.setResponse(response);
						//保存上下文
						InternalApiUtils.saveCurrentContext(ctx);
						try {
							for (AbstractInHandler inHandler : inHanlders) {
								boolean hande = inHandler.isHande(reqHead);
								if(hande) {
									inHandler.doHandle(reqHead, serviceData, ctx);
								}
							}
							//根据请求头信息，取spring service bean 进行方法调用
							Object serviceBean = this.act.getBean(serviceName);
							if(serviceBean!=null) {
								//判断serviceBean是否存在指定的serviceMethod方法
								Class<? extends Object> clazz = serviceBean.getClass();
								Method targetMethod = null;
								//优化采用参数为String的匹配方法
								try {
									try {
										targetMethod = clazz.getDeclaredMethod(serviceMethod, String.class);
									} catch (NoSuchMethodException | SecurityException e) {

									}
									if(targetMethod==null) {
										Method[] methods = clazz.getMethods();
										if(methods!=null) {
											for (Method method : methods) {
												if(method.getName().equals(serviceMethod)) {
													targetMethod = method;
													break;
												}
											}
										}
									}
									if(targetMethod!=null) {
										Object params = null;
										//判断目标方法是否为支持String参数类型，如果为复杂类型（自定义类型），则将请求数据serviceData转换javaBean后作为service method入参
										Class<?>[] parameterTypes = targetMethod.getParameterTypes();
										if(parameterTypes.length==1) {
											if(String.class.equals(parameterTypes[0])) {
												params = serviceData;
											}else{//入参转换vo，同时进行简单的参数校验（暂无）
												params = JSON.parseObject(serviceData, parameterTypes[0]);
											}
											Object resObj = targetMethod.invoke(serviceBean, params);
											resBody = this.createSuccessResponse(reqId,resObj);
										}else if(parameterTypes.length>1) {
											//处理方法参数个数不支持
											throw new InternalApiException(null,HandleStatus.目标服务参数个数不支持);
										}
									}else {
										//处理方法未找到
										throw new InternalApiException(null, HandleStatus.目标服务不存在);
									}
								} catch (SecurityException e) {
									throw new InternalApiException(e, HandleStatus.目标服务不存在);
								}
							}
						}catch (InternalApiException e) {
							Throwable srcEx = e.getSrcEx();
							ex = srcEx!=null?srcEx:e;
							resBody = this.createFailResponse(reqId,e.getHandleStatus());
						}
					}else {
						resBody = this.createFailResponse(reqId, HandleStatus.请求头参数错误);
					}
				}else {
					resBody = this.createFailResponse(reqId, HandleStatus.reqHead为空);
				}
			}else {
				resBody = this.createFailResponse(null, HandleStatus.请求报文为空);
			}
		} catch (Exception e) {
			ex = e;
			resBody = this.createFailResponse(null, HandleStatus.内部调用错误);
		}finally {
			if(ctx!=null) {
				//清除上下文
				InternalApiUtils.clearContext();
			}
			if(ex==null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date endTime = new Date();
				String reqTimeStr = sdf.format(reqTime);
				String recvTimeStr = sdf.format(recvTime);
				String endTimeStr = sdf.format(endTime);
				Long recvDiff = recvTime.getTime()-reqTime.getTime(); 
				Long handleDiff = endTime.getTime()-recvTime.getTime(); 
				logger.info("[内部接口API]-[请求报文:"+reqData+"]-[响应报文:"+resBody+"]-[发起调用时间:"+reqTimeStr+",本地接收请求时间:"+recvTimeStr+",本地响应时间:"+endTimeStr+"]-[接收耗时："+recvDiff+"ms,处理耗时,总耗时:"+handleDiff+"ms]");
			}else {
				logger.error("[内部接口API]-[接口调用异常]-[请求报文:"+reqData+"]",ex);
			}
		}
		return resBody;
	}
	
	/**
	 * 	创建失败返回报文
	 * @param resId
	 * @param errCode
	 * @param errMsg
	 * @return
	 */
	private String createFailResponse(String resId,HandleStatus handleStatus) {
		Assert.isTrue(handleStatus!=HandleStatus.成功, "内部接口调用失败响应不能为成功状态");
		return createResponse(resId, handleStatus, null);
	}
	/**
	 * 	创建成功返回报文
	 * @param resId
	 * @param resObj
	 * @return
	 */
	private String createSuccessResponse(String resId, Object resObj) {
		return createResponse(resId, HandleStatus.成功, resObj);
	}
	private String createResponse(String resId,HandleStatus handleStatus,Object resObj) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		JSONObject resJson = new JSONObject();
		JSONObject resHeadJson = new JSONObject();
//		string	Y	响应编号
		resHeadJson.put("res_id", resId);
//		string	Y	响应时间 日期格式：yyyy-MM-dd HH:mm:ss.SSS
		resHeadJson.put("res_time",sdf.format(new Date()));
//		string 	Y	分布式服务器IP地址
		resHeadJson.put("serv_ip", serverIp);
//		string 	Y	分布式服务器端口
		resHeadJson.put("serv_port",port);
		resHeadJson.put("res_code",handleStatus.resCode );
		resHeadJson.put("res_msg", handleStatus.resMsg);
		resJson.put("res_head", resHeadJson);
		if(resObj!=null) {
			if(resObj instanceof String) {
				resJson.put("res_body", resObj);
			}else {
				resJson.put("res_body", JSON.toJSONString(resObj));
			}
		}
		return resJson.toJSONString();
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.act = applicationContext;
		try {
			this.serverIp = NetworkUtils.getLocalHostLANAddress().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
}
