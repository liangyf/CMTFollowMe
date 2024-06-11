/**
 * 
 */
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
 * @author liangyf 设备号: 06D22007050383
 */
@RestController
@RequestMapping(value = "/device")
public class DeviceController {

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private RedisService redis;
	/**
	 */
	@PostPopulateMapping(value = "/collect", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject collect(@UserInfoOnion JSONObject request) {
		deviceService.saveDataFromHome(request);
		redis.cacheDataFromDevice(request);
		return new JSONObject();
	}

	/**
	 * 绑定设备 App使用
	 * 
	 * @return
	 */
	@PostPopulateMapping(value = "/bind", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject bind(@UserInfoOnion JSONObject reqBody) {
		deviceService.bind(reqBody);
		return new JSONObject();
	}

	/**
	 * 设备解绑 IVI调用
	 * 
	 */
	@PostPopulateMapping(value = "/unbind", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public JSONObject unbind(@UserInfoOnion JSONObject requestBody) {
		deviceService.unbind(requestBody.getString("username"), requestBody.getString("deviceId"));
		return new JSONObject();
	}

	/**
	 */
	@GetPopulateMapping(value = "/list")
	public JSON list(@UserInfoOnion(true) String username) {
		List<JSONObject> devices = deviceService.list(username);
		JSONArray result=new JSONArray();
		result.addAll(devices);
		return result;
	}

	/**
	 * @return
	 */
	@GetPopulateMapping(value = "/history")
	public JSON history(@UserInfoOnion(true) String username) {
		List<JSONObject> records = deviceService.historyHome(username, "");
		JSONArray result=new JSONArray();
		result.addAll(records);
		return result;
	}

	@PostPopulateMapping(value = "/deletedata", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
	public JSONObject delete(@UserInfoOnion JSONObject requestBody) {
		Integer[] set = requestBody.getJSONArray("ids").toArray(new Integer[0]);
		String username=requestBody.getString("username");
		long updatetime=requestBody.getLongValue("updatetime");
		deviceService.removeHistoryHome(username,updatetime,set);
		return new JSONObject();
	}

}
