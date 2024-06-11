package com.neusoft.rnma.ichpoc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.annotations.GetPopulateMapping;
import com.neusoft.rnma.ichpoc.annotations.PostPopulateMapping;
import com.neusoft.rnma.ichpoc.annotations.UserInfoOnion;
import com.neusoft.rnma.ichpoc.services.BasicService;

/**
 *
 * @author liangyf
 */
@RestController
@CrossOrigin
public class SystemController {

	@Autowired
	private BasicService basicService;

	/**
	 */
	@PostPopulateMapping(value = "/signup", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject signup(@RequestBody JSONObject request) {
		basicService.saveAccount(request);
		return new JSONObject();
	}

	/**
	 */
	@PostPopulateMapping(value = "/signin", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject signin(@RequestBody JSONObject request) {
		JSONObject data = new JSONObject();
		data.put("token", basicService.verifyUser(request.getString("username"), request.getString("password")));
		return data;
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@GetPopulateMapping(value = "/setting")
	public JSONObject myProfile(@UserInfoOnion(true) String username) {
		return basicService.getProfile(username);
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@PostPopulateMapping(value = "/agree", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject argree(@UserInfoOnion(true) String username) {
		basicService.upgradeAuth(username, 1);
		return new JSONObject();
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@PostPopulateMapping(value = "/cancel", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject cancel(@UserInfoOnion(true) String username) {
		basicService.upgradeAuth(username, 0);
		return new JSONObject();
	}

	/**
	 * 
	 * @param token
	 * @param setting
	 * @return
	 */
	@PostPopulateMapping(value = "/upgradesetting", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject upgradeSetting(@UserInfoOnion(true) String username, @RequestParam("setting") int setting) {
		basicService.upgradeSetting(username, setting);
		return new JSONObject();
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@GetPopulateMapping(value = "/pull1")
	public JSON pull1(@UserInfoOnion(true) String username) {
		JSONArray result = new JSONArray();
		result.addAll(basicService.pull1(username));
		return result;
	}

	@GetPopulateMapping(value = "/pull2")
	public JSON pull2(@UserInfoOnion(true) String username) {
		JSONArray result = new JSONArray();
		result.addAll(basicService.pull2(username));
		return result;
	}

	@GetPopulateMapping(value = "/pull3")
	public JSON pull3(@UserInfoOnion(true) String username) {
		JSONArray result = new JSONArray();
		result.addAll(basicService.pull3(username));
		return result;
	}

	@GetPopulateMapping(value = "/signout")
	public JSONObject signout(@RequestHeader(value = "token") String token) {
		basicService.cleanCache(token);
		return new JSONObject();
	}
	
	@PostPopulateMapping(value="/delhealthreport")
	public JSONObject deleteHealthReport(@UserInfoOnion JSONObject requestBody){
		Integer[] set = requestBody.getJSONArray("ids").toArray(new Integer[0]);
		basicService.deleteHealthReport(set);
		return new JSONObject();
	}

	@GetPopulateMapping(value = "/tags")
	public JSON tags(@RequestHeader(value = "token") String token) {
		List<JSONObject> data = basicService.tags();
		return JSONArray.parseArray(JSON.toJSONString(data));
	}
}
