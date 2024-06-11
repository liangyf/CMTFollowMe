package com.neusoft.rnma.ichpoc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.annotations.GetPopulateMapping;
import com.neusoft.rnma.ichpoc.annotations.PostPopulateMapping;
import com.neusoft.rnma.ichpoc.annotations.UserInfoOnion;
import com.neusoft.rnma.ichpoc.services.DeviceService;
import com.neusoft.rnma.ichpoc.services.RedisService;

/**
 * @author liangyf
 *
 */
@RestController
@RequestMapping(value = "/ivi")
public class IVIController {

	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private RedisService redis;

	@PostPopulateMapping(value = "/collect", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject collect(@UserInfoOnion JSONObject request) {
		deviceService.saveDataFromIVI(request);
		redis.cacheDataFromIVI(request);
		return new JSONObject();
	}

	/**
	 * 
	 */
	@GetPopulateMapping(value = "/history")
	public JSON history(@UserInfoOnion(true) String username) {
		List<JSONObject> records = deviceService.historyIVI(username);
		JSONArray result=new JSONArray();
		result.addAll(records);
		return result; 
	}

	@PostPopulateMapping(value = "/deletedata", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public JSONObject delete(@UserInfoOnion JSONObject requestBody) {
		Integer[] set = requestBody.getJSONArray("ids").toArray(new Integer[0]);
		String username=requestBody.getString("username");
		long updatetime=requestBody.getLongValue("updatetime");
		deviceService.removeHistoryIVI(username,updatetime,set);
		return new JSONObject();
	}

}
