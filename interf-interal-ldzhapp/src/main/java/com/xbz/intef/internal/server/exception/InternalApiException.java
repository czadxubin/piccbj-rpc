package com.xbz.intef.internal.server.exception;

import com.xbz.intef.internal.HandleStatus;

/**
 * 	执行请求处理期间异常基类
 * @author 许宝众
 *
 */
public class InternalApiException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 	原始异常信息
	 */
	private Throwable srcEx;
	/**
	 * 调用失败状态，默认{@link com.xbz.intef.internal.HandleStatus.内部调用错误}
	 */
	private HandleStatus handleStatus = HandleStatus.内部调用错误;
	
	/**
	 * 
	 * @param srcEx
	 * 		原始异常对象
	 * @param errCode
	 * 		异常错误代码
	 * @param errMsg
	 * 		异常错误信息
	 */
	public InternalApiException(Throwable srcEx, HandleStatus handleStatus) {
		this.srcEx = srcEx;
		this.handleStatus = handleStatus;
	}

	public HandleStatus getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(HandleStatus handleStatus) {
		this.handleStatus = handleStatus;
	}

	public Throwable getSrcEx() {
		return srcEx;
	}

	public void setSrcEx(Throwable srcEx) {
		this.srcEx = srcEx;
	}
}
