package com.neusoft.rnma.ichpoc.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.utils.CodeSet;
import com.neusoft.rnma.ichpoc.utils.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(2)
@WebFilter(filterName = "authFilter", urlPatterns = "/*")
public class JWTAuthFilter extends HttpFilter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3689750300873269414L;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CodeSet codeset;

	@Value("#{'${jwt.excludes}'.split(',')}")
	private List<String> excludeTable;

	@Override
	public void init() throws ServletException {
		System.out.println("Initialize the filter of token validation");
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		boolean interrupted = false;
		log.debug("ִexecute the filter of token validation");
		res.setHeader("Content-type", "application/json");
		res.setCharacterEncoding("UTF-8");
		res.setStatus(HttpServletResponse.SC_OK);
		String uri = req.getRequestURI();
		String remoteAddr = req.getRemoteAddr();
		log.info("Request {} from {} incomming",uri,remoteAddr);
//		String rootURI=uri.substring(0, uri.indexOf("/"));
		String rootURI=uri.split("/")[1];
		String token = req.getHeader("token");
		if (!excludeTable.contains(rootURI)) {
			if (StringUtils.isEmpty(token)) {
				// 未携带令牌
				log.error("Token is empty on request {} from {}: {}", uri, remoteAddr, token);
				interrupted = true;
			} else {
				String usernameInMemory = cacheManager.getCache("t-u").get(token, String.class);
				try {
					String usernameInToken = jwtTokenUtil.getUsernameFromToken(token);
					if (!usernameInToken.equals(usernameInMemory)) {
						// 缓存不存在TOKEN
						log.error("Token is mismatch on request {} from {}: {}", uri, remoteAddr, token);
						interrupted = true;
					}
				} catch (MalformedJwtException e) {
					// 令牌格式被破坏
					log.error("Token had been broken on {} from {}: {}", uri, remoteAddr, token);
					interrupted = true;
				} catch (ExpiredJwtException e) {
					// 令牌过期
					log.error("Token was expired on {} from {}: {}", uri, remoteAddr, token);
					interrupted = true;
				} catch (Exception e) {
					log.error("Unknown exception on {} from {}: {}", uri, remoteAddr, token);
					interrupted = true;
				}
			}
		} else {
			// 注册或登录不需要鉴权
		}
		if (interrupted) {
			JSONObject body = new JSONObject();
			body.put("status", codeset.getTokenInvalid());
			body.put("message", "Fail");
			if (!StringUtils.isEmpty(token))
				cacheManager.getCache("t-u").evictIfPresent(token);
			res.getWriter().print(body);
			return;
		}
		log.info("Everything was ok on {} from {}", uri, remoteAddr);
		chain.doFilter(req, res);
	}

	public void destroy() {
		System.out.println("Destroy the token cache");
		cacheManager.getCache("t-u").clear();
	}
}
