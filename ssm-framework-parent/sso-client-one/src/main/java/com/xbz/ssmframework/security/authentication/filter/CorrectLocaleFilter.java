package com.xbz.ssmframework.security.authentication.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 该过滤器用来纠正Spring Security可能出现去的本地化对象出错，造成翻译问题
 * @author xbz
 *
 */
public class CorrectLocaleFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		LocaleContextHolder.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
		chain.doFilter(request, response);
	}

}
