package com.neusoft.rnma.ichpoc.services;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liangyf
 */
public interface BasicService {


//    public int saveAccount(AccountRepository account);
    public void saveAccount(JSONObject account);

	public JSONObject getProfile(String username);

	public int upgradeSetting(String username,int setting);

	public String loadCredentialsByUsername(String username);

	public void upgradeAuth(String username,int agree);

	public List<JSONObject> pull1(String username);

	public List<JSONObject> tags();

	public List<JSONObject> pull2(String username);

	public List<JSONObject> pull3(String username);

	public String verifyUser(String string, String string2);

	public void cleanCache(String token);

	public void deleteHealthReport(Integer[] id);

}
