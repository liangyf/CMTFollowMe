/**
 * 
 */
package com.neusoft.rnma.ichpoc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.mapper.MockMapper;
import com.neusoft.rnma.ichpoc.services.MockService;

/**
 * @author liangyf
 *
 */
@Service
public class MockServiceImpl implements MockService {

	@Autowired
	private MockMapper mockMapper;

	@Override
	public void health(JSONObject request) {
		mockMapper.mockNotification(request);
	}

	@Override
	public void mockHealthReport(JSONObject repo) {
		mockMapper.saveHealthReportMock(repo);
	}

	@Override
	public void assis(JSONObject repo) {
		List<JSONObject> candidateSet = mockMapper.listCandidate();
		JSONObject elect = candidateSet.get((int) (Math.random() * candidateSet.size()));
		repo.put("content", elect.getString("content"));
		repo.put("title", elect.getString("title"));
		mockMapper.saveAssistant(repo);
	}

}
