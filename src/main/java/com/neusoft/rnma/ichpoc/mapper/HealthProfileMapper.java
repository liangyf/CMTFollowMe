/**
 * 
 */
package com.neusoft.rnma.ichpoc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liangyf
 *
 */
@Mapper
public interface HealthProfileMapper {
	public int upgradeProfile(JSONObject profile);

	public int upgradeBasicProfile(JSONObject profile);

	public JSONObject getBasicProfile(String username);

	public JSONObject getScores(String username);

	/**
	 * 获取用户名、健康状态、血压、心率、呼吸率
	 * 
	 * @param username
	 * @return
	 */
	public JSONObject summary(String username);

	/**
	 * 获取与用户相关的健康贴士
	 * 
	 * @param username
	 * @return
	 */
	public List<JSONObject> tips(String username);

	public int upgradeDataFromDevice(JSONObject dataRepository);

	public String tags(String username);

	public void addTag(String username,String 	result);

	public List<JSONObject> reports(String username);

	public List<? extends JSONObject> collectArticlesByTag(String tag);

	public JSONObject article(int id);
}
