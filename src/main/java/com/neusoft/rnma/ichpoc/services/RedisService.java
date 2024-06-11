/**
 * 
 */
package com.neusoft.rnma.ichpoc.services;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liangyf
 *
 */
public interface RedisService {

	void cacheDataFromDevice(JSONObject request);

	void cacheDataFromIVI(JSONObject request);

}
