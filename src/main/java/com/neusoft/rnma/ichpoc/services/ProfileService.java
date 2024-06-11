package com.neusoft.rnma.ichpoc.services;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface ProfileService {

	/**
	 * 更新健康档案数据
	 * 
	 * @param profileRepository
	 * @return
	 */
	public void upgradeBasicProfile(JSONObject profileRepository);

	/**
	 * 健康球数据
	 * 
	 * @param username
	 * @return
	 */
	public JSONObject getScores(String username);

	/**
	 * 主页数据
	 * 
	 * @param username
	 * @return
	 */
	public JSONObject summary(String username);

	/**
	 * 更新家用设备采集的数据到健康档案表
	 * 
	 * @param dataRepository
	 * @return
	 */
	public int upgradeDataFromDevice(JSONObject dataRepository);

	/**
	 * 用户的健康标签
	 * 
	 * @param usernameFromToken
	 * @return
	 */
	public JSONArray myTags(String usernameFromToken);

	public List<JSONObject> reports(String username);

	public List<JSONObject> articles(String username);

	public JSONObject article(int id);
}
