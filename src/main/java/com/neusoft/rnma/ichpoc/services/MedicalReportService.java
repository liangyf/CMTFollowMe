package com.neusoft.rnma.ichpoc.services;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface MedicalReportService {

	public JSONObject view(Integer id);
//	public MedicalReportRepository view(Integer id);

	public List<JSONObject> list(String username);

}
