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
public interface MockMapper {

	void mockNotification(JSONObject request);

	void saveHealthReportMock(JSONObject repo);

	List<JSONObject> listCandidate();

	void saveAssistant(JSONObject repo);

}
