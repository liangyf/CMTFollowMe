package com.neusoft.rnma.ichpoc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.alibaba.fastjson.JSONObject;

@Mapper
public interface MedicalReportMapper {

//	int save(JSONObject body);

	JSONObject viewReport(Integer id);

	List<JSONObject> list(String username);

	Integer[] getAvailable();

	JSONObject getMock(int id);

	int createMR(JSONObject mock);

	void reset();

	void removeMR(String username);
	
}
