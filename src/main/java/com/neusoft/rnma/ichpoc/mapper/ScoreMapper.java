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
/**
 * @author liangyf
 *
 */
@Mapper
public interface ScoreMapper {

	/**
	 * 更新血压评分
	 */
	public void upgradeBp(String username,int score);

	/**
	 * 更新体检报告评分
	 */
	public void upgradePe(String username,int score);

	/**
	 * 更新生活方式评分
	 */
	public void upgradeLive(String username,int score);

	/**
	 * 更新疲劳评分
	 */
	public void upgradeTired(String username,int score);

	/**
	 * 更新心脏评分
	 */
	public void upgradeHr(String username,int score);

	/**
	 * 获取指定用户48小时内最新的10条血压记录
	 * @param string
	 * @return
	 */
	public List<JSONObject> collectRecentOfBp(String username,Long offset);

	/**
	 * 获取指定用户48小时内最新的10条IVI记录
	 * @param username
	 * @param offset
	 * @return
	 */
	public List<JSONObject> collectRecentOfIVI(String username, Long offset);

	/**获取指定用户24小时内疲劳次数
	 * @param username
	 * @param offset
	 * @return
	 */
	public int timeOfTired(String username, Long offset);

	public int collectAbnormal(String username);

	public int getLiveScore(String string);
}
