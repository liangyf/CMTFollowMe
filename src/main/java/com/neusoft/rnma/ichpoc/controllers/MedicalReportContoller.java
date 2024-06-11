/**
 * 
 */
package com.neusoft.rnma.ichpoc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.annotations.GetPopulateMapping;
import com.neusoft.rnma.ichpoc.annotations.UserInfoOnion;
import com.neusoft.rnma.ichpoc.services.MedicalReportService;

/**
 * @author liangyf
 *
 */
@RestController
@RequestMapping(value = "/mr")
public class MedicalReportContoller {

	@Autowired
	private MedicalReportService mrService;

	/**
	 * @return
	 */
	@GetPopulateMapping(value = "/view")
	public JSONObject view(@UserInfoOnion(true) String username, @RequestParam Integer id) {
		return mrService.view(id);
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	@GetPopulateMapping(value = "/list")
	public JSON list(@UserInfoOnion(true) String username) {
		List<JSONObject> list = mrService.list(username);
		JSONArray result=new JSONArray();
		result.addAll(list);
		return result;
	}

}
