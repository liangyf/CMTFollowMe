/**
 * 
 */
package com.neusoft.rnma.ichpoc.services;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liangyf
 *
 */
public interface DeviceService {

	/**
	 * 绑定家用设备
	 * 
	 * @param deviceRepository
	 * @return
	 */
	public int bind(JSONObject deviceRepository);

	/**
	 * 解绑家用设备
	 * 
	 * @param username
	 * @param deviceId
	 * @return
	 */
	public int unbind(String username, String deviceId);

	/**
	 * 家用设备绑定列表
	 * 
	 * @param username
	 * @return
	 */
	public List<JSONObject> list(String username);

	/**
	 * 保存家用设备采集数据
	 * 
	 * @param dataRepository
	 * @return
	 */
	public void saveDataFromHome(JSONObject dataRepository);

	/**
	 * 浏览家用设备采集记录
	 * 
	 * @param username
	 * @param deviceId
	 * @return
	 */
	public List<JSONObject> historyHome(String username, String deviceId);

	/**
	 * 删除家用设备的采集记录
	 * 
	 * @param set
	 * @return
	 */
	public boolean removeHistoryHome(String username,Long updatetime,Integer[] set);

	/**
	 * 保存IVI测量数据
	 * 
	 * @param repo
	 */
	public void saveDataFromIVI(JSONObject repo);

	public List<JSONObject> historyIVI(String username);

	public void removeHistoryIVI(String username,Long updatetime,Integer[] set);

}
