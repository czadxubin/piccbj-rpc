package com.xbz.intef.internal.server.handlers;

import javax.servlet.http.HttpServletRequest;

import com.xbz.intef.internal.model.InternalApiReqHead;
import com.xbz.intef.internal.server.exception.InternalApiException;

/**
 * 	抽象内部接口处理器
 * @author 许宝众
 *
 */
public abstract class AbstractInHandler {
	private int order;
	/**
	 * 	子类实现此接口，是否需要处理当前请求
	 * @param reqHead
	 */
	public abstract boolean isHande(InternalApiReqHead reqHead);
	
	/**
	 * 	执行处理，当{@link #isHande(InternalApiReqHead)}返回true时调用此方法
	 * @param reqHead
	 * 			请求头
	 * @param serviceData
	 * 			请求的业务数据
	 * @param ctx
	 * 			执行链上下文
	 * @throws InternalApiException
	 * 		执行链执行期间发生此异常不在向下执行
	 */
	public abstract void doHandle(InternalApiReqHead reqHead,String serviceData,InternalApiContext ctx) throws InternalApiException;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
