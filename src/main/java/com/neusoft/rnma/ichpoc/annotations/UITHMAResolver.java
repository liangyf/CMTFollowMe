/**
 * 
 */
package com.neusoft.rnma.ichpoc.annotations;

import java.io.BufferedReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liangyf
 *
 */
@Component
@Configuration
@CacheConfig(cacheNames = "t-u")
@Slf4j
public class UITHMAResolver implements HandlerMethodArgumentResolver, WebMvcConfigurer {

	@Autowired
	private CacheManager cacheManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#
	 * supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UserInfoOnion.class)
				&& (parameter.getParameterType().isAssignableFrom(String.class)
						|| parameter.getParameterType().isAssignableFrom(JSONObject.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#
	 * resolveArgument(org.springframework.core.MethodParameter,
	 * org.springframework.web.method.support.ModelAndViewContainer,
	 * org.springframework.web.context.request.NativeWebRequest,
	 * org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String token = webRequest.getHeader("token");
		String username = cacheManager.getCache("t-u").get(token, String.class);
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		boolean plain = parameter.getParameterAnnotation(UserInfoOnion.class).value();
		if (!plain) {
			BufferedReader br = request.getReader();
			String line = br.readLine();
			StringBuffer jsonStringBuffer = new StringBuffer();
			while (line != null) {
				jsonStringBuffer.append(line);
				line = br.readLine();
			}
			JSONObject requestBody = JSONObject.parseObject(jsonStringBuffer.toString());
			if (requestBody == null)
				requestBody = new JSONObject();
			requestBody.put("username", username);
			long time = System.currentTimeMillis();
			requestBody.put("createtime", time);
			requestBody.put("updatetime", time);
			log.info("REQ:{}", requestBody.toJSONString());
			return requestBody;
		} else {
			return username;
		}
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(this);
	}
}
