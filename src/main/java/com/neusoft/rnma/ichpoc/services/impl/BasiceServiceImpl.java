package com.neusoft.rnma.ichpoc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.mapper.HealthProfileMapper;
import com.neusoft.rnma.ichpoc.mapper.MedicalReportMapper;
import com.neusoft.rnma.ichpoc.mapper.ScoreMapper;
import com.neusoft.rnma.ichpoc.mapper.SystemMapper;
import com.neusoft.rnma.ichpoc.mapper.TagsMapper;
import com.neusoft.rnma.ichpoc.services.BasicService;
import com.neusoft.rnma.ichpoc.utils.BCryptPasswordEncoder;
import com.neusoft.rnma.ichpoc.utils.EnumSet;
import com.neusoft.rnma.ichpoc.utils.JwtTokenUtil;

/**
 * @author liangyf
 */
//@Slf4j
@Service
@CacheConfig(cacheNames = "t-u")
public class BasiceServiceImpl implements BasicService {
	@Autowired
	private SystemMapper systemMapper;
	@Autowired
	private MedicalReportMapper mrMapper;

	@Autowired
	private HealthProfileMapper healthProfileMapper;

	@Autowired
	private ScoreMapper scoreMapper;

	@Autowired
	private TagsMapper tagsMapper;

	@Autowired
	private HealthProfileMapper profileMapper;

	@Autowired
	private EnumSet enumSet;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private CacheManager cacheManager;

	@Override
	public String loadCredentialsByUsername(String username) {
		return systemMapper.getCredentialByUsername(username);
	}

	@Override
	@Transactional
	public void saveAccount(JSONObject account) {
		// 添加一条用户(xx_user)表记录
		account.put("password", pe.encode(account.getString("password")));
		account.put("createtime", System.currentTimeMillis());
		systemMapper.insertAccount(account);

	}

	@Override
	public JSONObject getProfile(String username) {
		return systemMapper.getProfile(username);
	}

	@Override
	public int upgradeSetting(String username, int setting) {
		return systemMapper.upgradeSetting(username, setting);
	}

	@Override
	@Transactional
	public void upgradeAuth(String username, int agree) {
		int result = systemMapper.upgradeAuthorized(username, agree);
		if (result == 0)
			return;
		int score = 0;
		if (agree == 1) {
			// 随机在xx_xx_medical_report_third_resource表抓取一条数据作为用户的体检报告
			Integer[] available = mrMapper.getAvailable();
			if (available.length < 1) {
				mrMapper.reset();
				available = mrMapper.getAvailable();
			}
//			// 添加一条体检报告(xx_medical_report)
			int idx = (int) (Math.random() * available.length);
			JSONObject mock = mrMapper.getMock(available[idx]);
			mock.put("username", username);
			mock.put("bp_fromstr", enumSet.getSourceSp());
			long time = System.currentTimeMillis();
			mock.put("createtime", time);
			mrMapper.createMR(mock);
			// 添加一条健康记录
			healthProfileMapper.upgradeProfile(mock);

			int abnormalCount = scoreMapper.collectAbnormal(username);
			score = 100 - 8 * abnormalCount;
			if (score < 0)
				score = 0;
			// 根据年龄更新标签
			JSONArray tags = JSONObject.parseArray(profileMapper.tags(username));
			JSONObject ageTag70 = new JSONObject();
			ageTag70.put("result", "70后");
			ageTag70.put("proposal", "根据年龄判断");
			JSONObject ageTag80 = new JSONObject();
			ageTag80.put("result", "80后");
			ageTag80.put("proposal", "根据年龄判断");
			JSONObject ageTag85 = new JSONObject();
			ageTag85.put("result", "85后");
			ageTag85.put("proposal", "根据年龄判断");
			JSONObject ageTag90 = new JSONObject();
			ageTag90.put("result", "90后");
			ageTag90.put("proposal", "根据年龄判断");
			tags.remove(ageTag70);
			tags.remove(ageTag80);
			tags.remove(ageTag85);
			tags.remove(ageTag90);
			if (mock.getInteger("basic_age") > 51) {
				tags.add(ageTag70);
			} else if (mock.getInteger("basic_age") > 41) {
				tags.add(ageTag80);
			} else if (mock.getInteger("basic_age") > 36) {
				tags.add(ageTag85);
			} else if (mock.getInteger("basic_age") > 31) {
				tags.add(ageTag90);
			}
			profileMapper.addTag(username, tags.toJSONString());
		} else if (agree == 0) {
			mrMapper.removeMR(username);
			score = -1;
		}
		scoreMapper.upgradePe(username, score);
	}

	@Override
	public List<JSONObject> pull1(String username) {
		return systemMapper.pull1(username);
	}

	@Override
	public List<JSONObject> tags() {
		return tagsMapper.all();
	}

	@Override
	public List<JSONObject> pull2(String username) {
		return systemMapper.pull2(username);
	}

	@Override
	public List<JSONObject> pull3(String username) {
		return systemMapper.pull3(username);
	}

	@Override
	public String verifyUser(String username, String password) {
		String credentials = loadCredentialsByUsername(username);
		if (pe.matches(password, credentials)) {
			// cache token and username
			String token = tokenUtil.generateToken(username);
			cacheManager.getCache("t-u").put(token, username);
			return token;
		} else {
			throw new IllegalArgumentException("password is wrong");
		}
	}

	@Override
	public void cleanCache(String token) {
		cacheManager.getCache("t-u").evictIfPresent(token);
	}

	@Override
	public void deleteHealthReport(Integer[] id) {
		systemMapper.deleteHealthReport(id);
	}
}
