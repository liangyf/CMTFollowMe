/**
 * 
 */
package com.neusoft.rnma.ichpoc.annotations;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.utils.CodeSet;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liangyf
 *
 */
@Component
@Aspect
@Slf4j
public class PopulateInterceptor {

	@Autowired
	CodeSet codeset;

	public PopulateInterceptor() {

	}

	@Pointcut("@annotation(com.neusoft.rnma.ichpoc.annotations.GetPopulateMapping)")
	private void getMethod() {
	}

	@Pointcut("@annotation(com.neusoft.rnma.ichpoc.annotations.PostPopulateMapping)")
	private void postMethod() {
	}

	@Around("getMethod()")
	public Object get(ProceedingJoinPoint joinPoint) {
		log.debug("populate the method of {}", joinPoint.getSignature());
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String remoteAddr = request.getRemoteAddr();
		String uri = request.getRequestURI();
		JSONObject body = new JSONObject();
		try {
			body.put("data", joinPoint.proceed());
			body.put("status", codeset.getOk());
			body.put("message", "Success");
		} catch (DuplicateKeyException e) {
			log.error("DuplicateKeyException on request {} from {} : {}", uri, remoteAddr, joinPoint.getArgs());
			e.printStackTrace();
			body.put("status", codeset.getExist());
			body.put("message", "Exist");
		} catch (IllegalArgumentException e) {
			log.error("IllegalArgumentException on request {} from {} : {}", uri, remoteAddr, joinPoint.getArgs());
			e.printStackTrace();
			body.put("status", codeset.getUnauthorized());
			body.put("message", "Invalid");
		} catch (Throwable t) {
			log.error("UnknownException on request {} from {} : {}", uri, remoteAddr, joinPoint.getArgs());
			t.printStackTrace();
			body.put("status", codeset.getFail());
			body.put("message", "Fail");
		}
		log.info("RESP:{}", body.toJSONString());
		return body;
	}

	@Around("postMethod()")
	public Object post(ProceedingJoinPoint joinPoint) {
		return get(joinPoint);
	}

}
