package com.xbz.intef.internal.util;

import com.xbz.intef.internal.server.handlers.InternalApiContext;

public class InternalApiUtils {
	/**每次线程执行时上下文变量**/
	private static final InheritableThreadLocal<InternalApiContext> context = new InheritableThreadLocal<>();
	public static void clearContext() {
		context.remove();
	}
	public static void saveCurrentContext(InternalApiContext ctx) {
		context.set(ctx);
	}
	/**
	 * 	获取当前线程下的上下文对象
	 * @return
	 */
	public static InternalApiContext getCurrentContext() {
		return context.get();
	}
}
