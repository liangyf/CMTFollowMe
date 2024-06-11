package com.neusoft.rnma.ichpoc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.annotations.GetPopulateMapping;
import com.neusoft.rnma.ichpoc.annotations.PostPopulateMapping;
import com.neusoft.rnma.ichpoc.annotations.UserInfoOnion;
import com.neusoft.rnma.ichpoc.services.ProfileService;

/**
 * @author liangyf
 */
@RestController
@RequestMapping(value = "/profile")
@CrossOrigin
public class ProfileController {

	@Autowired
	private ProfileService profileService;

	@GetPopulateMapping(value = "/getscores")
	public JSONObject getScore(@UserInfoOnion(true) String username) {
		return profileService.getScores(username);
	}

	@GetPopulateMapping(value = "/summary")
	public JSONObject summary(@UserInfoOnion(true) String username) {
		return profileService.summary(username);
	}

	/**
	 * 
	 * @param token
	 * @param requestBody
	 * @return
	 */
	@PostPopulateMapping(value = "/upgradebasic", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	public JSONObject upgradeProfile(@UserInfoOnion JSONObject requestBody) {
		profileService.upgradeBasicProfile(requestBody);
		return new JSONObject();
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@GetPopulateMapping(value = "/mytags")
	public JSON tags(@UserInfoOnion(true) String username) {
		return profileService.myTags(username);
	}

	@GetPopulateMapping(value = "/reports")
	public JSON report(@UserInfoOnion(true) String username) {
		List<JSONObject> rs = profileService.reports(username);
		JSONArray result=new JSONArray();
		result.addAll(rs);
		return result;
	}

	@GetPopulateMapping(value = "/articles")
	public JSON articles(@UserInfoOnion(true) String username) {
		List<JSONObject> articles = profileService.articles(username);
		return JSONArray.parseArray(JSON.toJSONString(articles));
	}

	@GetPopulateMapping(value = "/article")
	public JSONObject article(@UserInfoOnion(true) String username, @RequestParam int id) {
		return profileService.article(id);
	}

}
