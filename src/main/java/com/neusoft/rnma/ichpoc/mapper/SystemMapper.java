package com.neusoft.rnma.ichpoc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liangyf
 */
@Mapper
public interface SystemMapper {

//	public int insertAccount(AccountRepository account);
	public void insertAccount(JSONObject account);

	public String getCredentialByUsername(String username);

	public JSONObject getProfile(String username);

	public int upgradeSetting(String username, int setting);

	public int upgradeAuthorized(String username,int agree);

	public List<JSONObject> pull1(String username);

	public List<JSONObject> pull2(String username);

	public List<JSONObject> pull3(String username);

	public void deleteHealthReport(Integer[] id);

}
