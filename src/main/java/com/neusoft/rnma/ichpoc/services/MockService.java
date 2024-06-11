/**
 * 
 */
package com.neusoft.rnma.ichpoc.services;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liangyf
 *
 */
public interface MockService {

	void health(JSONObject repo);

	void mockHealthReport(JSONObject repo);

	void assis(JSONObject repo);

}
