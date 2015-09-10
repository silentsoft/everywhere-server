package org.silentsoft.everywhere.server.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.silentsoft.core.util.ObjectUtil;
import org.silentsoft.everywhere.context.BizConst;
import org.silentsoft.everywhere.context.core.SharedThreadMemory;
import org.silentsoft.everywhere.context.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		if ("/fx/register/authentication".equals(request.getServletPath()) ||
			"/fx/login/authentication".equals(request.getServletPath())) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			if (isValidUser(request)) {
				SharedThreadMemory.create();
				
				SharedThreadMemory.put(BizConst.KEY_USER_ID, request.getHeader("user"));
				filterChain.doFilter(servletRequest, servletResponse);
				
				SharedThreadMemory.delete();
			} else {
				LOGGER.info("Detected not valid user on <{}> from <{}> !", new Object[]{request.getRequestURI(), request.getRemoteAddr()});
				
				HttpServletResponse response = (HttpServletResponse) servletResponse;
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "[WARNING] You are trying to access server by unknown way !!!");
			}
		}
	}
	
	public void destroy() {
		
	}

	private boolean isValidUser(HttpServletRequest request) {
		boolean result = false;
		
		/**
		 * Avoid header modulation attack from hacker
		 */
		String user = request.getHeader("user");
		String sequence = request.getHeader("sequence");
		if (ObjectUtil.isNotEmpty(user) && ObjectUtil.isNotEmpty(sequence)) {
			result = SecurityUtil.isPasswordValid(sequence, user);
		}
		
		return result;
	}
}
