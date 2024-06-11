/**
 * 
 */
package com.neusoft.rnma.ichpoc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.mapper.MedicalReportMapper;
import com.neusoft.rnma.ichpoc.services.MedicalReportService;
import com.neusoft.rnma.ichpoc.utils.LabelSet;

/**
 * @author liangyf
 *
 */

@Service
public class MedicalReportServiceImpl implements MedicalReportService {

	@Autowired
	private MedicalReportMapper mrMapper;
	
	@Autowired
	private LabelSet labels;
	/* (non-Javadoc)
	 * @see com.neusoft.rnma.ichpoc.services.MedicalReportService#view(java.lang.String)
	 */
	@Override
	public JSONObject view(Integer id) {
		JSONObject report=mrMapper.viewReport(id);
		report.put("mer_result", JSONObject.parseArray(report.getString("mer_result")));
		report.putAll(labels.getMrs());
		return report;
	}
	@Override
	public List<JSONObject> list(String username) {
		return mrMapper.list(username);
	}

}
