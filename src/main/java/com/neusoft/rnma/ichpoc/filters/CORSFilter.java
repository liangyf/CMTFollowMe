/**
 * 
 */
package com.neusoft.rnma.ichpoc.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author liangyf
 *
 */
@Deprecated
//@Order(1)
//@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
public class CORSFilter implements Filter {

	@Value("#{'${blackList}'.split(',')}")
	private List<String> blackTable;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		log.debug("CORSFilter");
//		HttpServletResponse resp = (HttpServletResponse) response;
//		HttpServletRequest req = (HttpServletRequest) request;
//		String remoteAddr = req.getRemoteAddr();
//		if (blackTable.contains(remoteAddr)) {
//			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
//			return;
//		}
//		resp.setHeader("Access-Control-Allow-Origin", "*");
//		resp.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
//		resp.setHeader("Access-Control-Max-Age", "3600");
//		resp.setHeader("Access-Control-Allow-Headers", "*");
//		if (req.getMethod().equals("OPTIONS")) {
//			resp.setStatus(HttpServletResponse.SC_OK);
//			return;
//		}
//		chain.doFilter(request, response);
	}

}
