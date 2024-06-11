/**
 * 
 */
package com.neusoft.rnma.ichpoc.controllers;

import java.util.Calendar;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.annotations.PostPopulateMapping;
import com.neusoft.rnma.ichpoc.annotations.UserInfoOnion;
import com.neusoft.rnma.ichpoc.services.MockService;

/**
 * @author liangyf
 *
 */
@RestController
@RequestMapping(value = "/mock")
public class MockController {
	@Autowired
	private MockService mockService;

	/**
	 * 
	 * @param token
	 * @param type
	 * @return
	 */
	@PostPopulateMapping(value = "/month", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject month(@UserInfoOnion JSONObject repo) {
		repo.put("type", "month");
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.add(Calendar.MONTH, -1);
		repo.put("title", String.format("%d年%d月健康分析报告", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1));
		mockService.mockHealthReport(repo);
		return new JSONObject();
	}

	/**
	 * 
	 * @param token
	 * @param type
	 * @return
	 */
	@PostPopulateMapping(value = "/quarter", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject season(@UserInfoOnion JSONObject repo) {
		repo.put("type", "quarter");
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.add(Calendar.MONTH, -3);
		repo.put("title", String.format("%d年第%d季度健康分析报告", c.get(Calendar.YEAR), c.get(Calendar.MONTH) / 3 + 1));
		mockService.mockHealthReport(repo);
		return new JSONObject();
	}

	/**
	 * 
	 * @param token
	 * @param type
	 * @return
	 */
	@PostPopulateMapping(value = "/year", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject year(@UserInfoOnion JSONObject repo) {
		repo.put("type", "year");
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.add(Calendar.YEAR, -1);
		repo.put("title", String.format("%d年健康分析报告", c.get(Calendar.YEAR)));
		mockService.mockHealthReport(repo);
		return new JSONObject();
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@PostPopulateMapping(value = "/health", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject health(@UserInfoOnion JSONObject repo) {
		repo.put("type", "体征异常预警");
		mockService.health(repo);
		return new JSONObject();
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@PostPopulateMapping(value = "/tired", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject tired(@UserInfoOnion JSONObject repo) {
		repo.put("type", "疲劳驾驶预警");
		mockService.health(repo);
		return new JSONObject();
	}

	/**
	 * 模拟健康设备数据异常
	 * 
	 * @param token
	 * @return
	 */
	@PostPopulateMapping(value = "/unusual", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject unusual(@UserInfoOnion JSONObject repo) {
		repo.put("type", "家用设备数据异常预警");
		mockService.health(repo);
		return new JSONObject();
	}

	@PostPopulateMapping(value = "/assis", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject assis(@UserInfoOnion JSONObject repo) {
		mockService.assis(repo);
		return new JSONObject();
	}
}
