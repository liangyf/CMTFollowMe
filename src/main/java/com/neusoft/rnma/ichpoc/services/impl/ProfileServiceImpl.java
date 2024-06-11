/**
 * 
 */
package com.neusoft.rnma.ichpoc.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.mapper.HealthProfileMapper;
import com.neusoft.rnma.ichpoc.mapper.ScoreMapper;
import com.neusoft.rnma.ichpoc.services.ProfileService;

/**
 * @author liangyf
 *
 */
@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	private HealthProfileMapper profileMapper;

	@Autowired
	private ScoreMapper scoreMapper;

	@Override
	@Transactional
	public void upgradeBasicProfile(JSONObject profile) {
		int affectCount = 0, goodCount = 0, badCound = 0;
		String diet = profile.getString("diet"), drink = profile.getString("drink"),
				exercise = profile.getString("exercise"), labor = profile.getString("labor"),
				misc = profile.getString("misc"), smoke = profile.getString("smoke");
		if (!StringUtils.isEmpty(diet)) {
			affectCount++;
			if (diet.indexOf("荤素均衡") >= 0)
				goodCount++;
			if (diet.indexOf("荤食为主") >= 0 || diet.indexOf("素食为主") >= 0)
				badCound++;
			if (diet.indexOf("嗜盐") >= 0)
				badCound++;
			if (diet.indexOf("嗜油") >= 0)
				badCound++;
			if (diet.indexOf("嗜糖") >= 0)
				badCound++;
		}
		if (!StringUtils.isEmpty(drink)) {
			affectCount++;
			if (drink.indexOf("从不饮酒") >= 0)
				goodCount++;
			else if (drink.indexOf("饮酒") >= 0)
				badCound++;
		}
		if (!StringUtils.isEmpty(exercise)) {
			affectCount++;
			if (exercise.indexOf("每天") >= 0 || exercise.indexOf("每周1次以上") >= 0)
				goodCount++;
			else if (exercise.indexOf("不运动") >= 0)
				badCound++;
		}
		if (!StringUtils.isEmpty(labor)) {
			affectCount++;
		}
		if (!StringUtils.isEmpty(misc)) {
			affectCount++;
			if (misc.indexOf("久坐") >= 0)
				badCound++;
			if (misc.indexOf("熬夜") >= 0)
				badCound++;
			if (misc.indexOf("久站") >= 0)
				badCound++;
			if (misc.indexOf("跷二郎腿") >= 0)
				badCound++;
			if (misc.indexOf("不喜喝水") >= 0)
				badCound++;
			if(misc.indexOf("强忍大便")>=0)
				badCound++;
		}
		if (!StringUtils.isEmpty(smoke)) {
			affectCount++;
			if (smoke.indexOf("从不吸烟") >= 0)
				goodCount++;
			else if (smoke.indexOf("吸烟") >= 0)
				badCound++;
		}
		byte changeStatus = 0;
		if (profile.getInteger("bmi") != null && profile.getInteger("bmi") > 0) {
			changeStatus |= 32;
			affectCount++;
		}
		if (profile.getInteger("height") != null && profile.getInteger("height") > 0) {
			changeStatus |= 16;
			affectCount++;
		}
		if (profile.getInteger("weight") != null && profile.getInteger("weight") > 0) {
			changeStatus |= 8;
			affectCount++;
		}
		if (profile.getInteger("hipsline") != null && profile.getInteger("hipsline") > 0) {
			changeStatus |= 4;
			affectCount++;
		}
		if (profile.getInteger("waistline") != null && profile.getInteger("waistline") > 0) {
			affectCount++;
			changeStatus |= 2;
		}
		if (profile.getInteger("age") != null && profile.getInteger("age") > 0) {
			affectCount++;
			changeStatus |= 1;
		}
		profile.put("lockBits", changeStatus);
		if (affectCount > 0) {
			profileMapper.upgradeBasicProfile(profile);
			int oldScore=scoreMapper.getLiveScore(profile.getString("username"));
			oldScore=oldScore<0?100:oldScore;
			int score = (oldScore - badCound * 10 + goodCount * 10) > 99 ? 100 : (oldScore - badCound * 10 + goodCount * 10);
			scoreMapper.upgradeLive(profile.getString("username"), score);
		}
	}

	@Override
	public JSONObject getScores(String username) {
		return profileMapper.getScores(username);
	}

	@Override
	public JSONObject summary(String username) {
		JSONObject summary = profileMapper.summary(username);
		summary.put("tips", profileMapper.tips(username));
		summary.put("now", System.currentTimeMillis());
		return summary;
	}

	@Override
	public int upgradeDataFromDevice(JSONObject dataRepository) {
		return profileMapper.upgradeDataFromDevice(dataRepository);
	}

	@Override
	public JSONArray myTags(String username) {
		String result = profileMapper.tags(username);
		return JSONArray.parseArray(result);
	}

	@Override
	public List<JSONObject> reports(String username) {
		return profileMapper.reports(username);
	}

	@Override
	public List<JSONObject> articles(String username) {
		JSONArray tags = myTags(username);
		Iterator<Object> i = tags.iterator();
		List<JSONObject> as = new ArrayList<JSONObject>();
		while (i.hasNext()) {
			JSONObject tag = (JSONObject) i.next();
			as.addAll(profileMapper.collectArticlesByTag(tag.getString("result")));
		}
		return as;
	}

	@Override
	public JSONObject article(int id) {
		JSONObject result = profileMapper.article(id);
		JSONArray tagArray = new JSONArray();
		tagArray.addAll(Arrays.asList(result.getString("tags").split(",")));
		result.put("tags", tagArray);
		return result;
	}
}
