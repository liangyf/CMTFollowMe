package com.neusoft.rnma.ichpoc.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.alibaba.fastjson.JSONObject;

/**
 * @author liangyf
 *
 */
@Mapper
public interface DeviceMapper {

	public int bind(JSONObject deviceRepository);

	public List<JSONObject> list(String username);

	public int saveRecordFromHome(JSONObject dataRepository);

	public List<JSONObject> historyHome(String username, String deviceId);

	public int unbind(String username, String deviceId);

	public boolean removeHistoryHome(Integer[] ids);

	public void saveRecordFromIVI(JSONObject repo);

	public List<JSONObject> historyIVI(String username);

	public boolean removeHistoryIVI(Integer[] set);

	public void rollbackHome(String username,Long updatetime);

	public void rollbackIVI(String username, Long updatetime);

	int iviHistoryCount(String username);

	void resetIvi(String username);

	int deviceHistoryCount(String username);

	void resetDevice(String username);
}
