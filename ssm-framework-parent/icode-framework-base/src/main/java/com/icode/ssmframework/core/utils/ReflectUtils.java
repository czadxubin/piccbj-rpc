package com.icode.ssmframework.core.utils;

public class ReflectUtils {
	/**
	 * 
	 * @param thread
	 * @return
	 */
	public static String getCurrentMethod() {
		Thread currentThread = Thread.currentThread();
		StackTraceElement[] stackTrace = currentThread.getStackTrace();
		return stackTrace[2].getMethodName();
	}
}
