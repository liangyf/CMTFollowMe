/**
 *
 */
package com.neusoft.rnma.ichpoc.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.rnma.ichpoc.mapper.DeviceMapper;
import com.neusoft.rnma.ichpoc.mapper.HealthProfileMapper;
import com.neusoft.rnma.ichpoc.mapper.ScoreMapper;
import com.neusoft.rnma.ichpoc.mapper.TagsMapper;
import com.neusoft.rnma.ichpoc.services.DeviceService;
import com.neusoft.rnma.ichpoc.utils.EnumSet;

/**
 * @author liangyf
 *
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private HealthProfileMapper profileMapper;

    @Autowired
    private TagsMapper tagsMapper;

    @Autowired
    private EnumSet enumSet;

    @Override
    public int bind(JSONObject deviceRepository) {
        return deviceMapper.bind(deviceRepository);
    }

    @Override
    public int unbind(String username, String deviceId) {
        return deviceMapper.unbind(username, deviceId);
    }

    @Override
    public List<JSONObject> list(String username) {
        return deviceMapper.list(username);
    }

    @Override
    @Transactional
    public void saveDataFromHome(JSONObject dataRepository) {
        dataRepository.put("source", enumSet.getSourceHome());
        // 保存到采集记录表
        int bph = dataRepository.getIntValue("bph");
        int bpl = dataRepository.getIntValue("bpl");
        String username = dataRepository.getString("username");
        String status = enumSet.getNormal();
        if (bph >= 180 || bpl >= 110) {
            // 重度高血压
            status = enumSet.getBpSevere();
        } else if (bph >= 160 || bpl >= 100) {
            status = enumSet.getBpMedium();
        } else if (bph >= 140 || bpl >= 90) {
            status = enumSet.getBpLittle();
        } else if (bph >= 120 || bpl >= 80) {
            status = enumSet.getBpTerminate();
        }
        dataRepository.put("status", status);
        deviceMapper.saveRecordFromHome(dataRepository);
        updateBpScoreAndTag(username);
    }

    @Override
    public List<JSONObject> historyHome(String username, String deviceId) {
        return deviceMapper.historyHome(username, deviceId);
    }

    @Override
    @Transactional
    public boolean removeHistoryHome(String username, Long updatetime, Integer[] set) {
        deviceMapper.removeHistoryHome(set);
        deviceMapper.rollbackHome(username, updatetime);
        int c=deviceMapper.deviceHistoryCount(username);
        if(c<1){
            deviceMapper.resetDevice(username);
        }
        updateBpScoreAndTag(username);
        return true;
    }

    @Override
    @Transactional
    public void saveDataFromIVI(JSONObject repo) {
        int tired = repo.getIntValue("tired");
        int conclusion = repo.getIntValue("conclusion");
        String username = repo.getString("username");
        switch (tired) {
            case 0:
                repo.put("tired", enumSet.getNormal());
                break;
            case 1:
                repo.put("tired", enumSet.getTired());
                break;
            default:
                throw new IllegalArgumentException("The available value of tired is only 0 or 1");
        }
        switch (conclusion) {
            case 0:
                repo.put("conclusion", enumSet.getNormal());
                break;
            case 1:
                repo.put("conclusion", enumSet.getAbnormalLittle());
                break;
            case 2:
                repo.put("conclusion", enumSet.getAbnormalSevere());
                break;
            default:
                throw new IllegalArgumentException("The available value of conclusion is only 0, 1 or 2");
        }
        deviceMapper.saveRecordFromIVI(repo);
        updateRelatedWithIvi(username);
    }

    private void updateRelatedWithIvi(String username) {// 收集过去48小时内的最新最多10条测量记录
        List<JSONObject> recentSet = scoreMapper.collectRecentOfIVI(username,
                System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000);
        if (recentSet.size() > 0) {// 如果48小时内有测量记录
            int tiredCount = 0, littleCount = 0, severeCound = 0, hrHigh = 0, hrLow = 0, brHigh = 0, brLow = 0;
            for (JSONObject r : recentSet) {
                if (enumSet.getTired().equals(r.getString("tired")))
                    tiredCount++;
                if (enumSet.getAbnormalSevere().equals(r.getString("conclusion")))
                    severeCound++;
                else if (enumSet.getAbnormalLittle().equals(r.getString("conclusion")))
                    littleCount++;
                int hr = r.getIntValue("hr");
                int br = r.getIntValue("br");
                if (hr >= 100)
                    hrHigh++;
                else if (hr <= 60)
                    hrLow++;
                if (br >= 20)
                    brHigh++;
                else if (br <= 12)
                    brLow++;
            }
            // 计算并保存疲劳分数
            int score = 100 - 100 / recentSet.size() * tiredCount;
            scoreMapper.upgradeTired(username, score);
            // 计算并保存心脏分数
            score = 100 - 50 / recentSet.size() * littleCount - 100 / recentSet.size() * severeCound;
            scoreMapper.upgradeHr(username, score);
            // 判断是否需要更新标签
            boolean tagsUpdated = false;
            int tiredTimeOfDay = scoreMapper.timeOfTired(username, System.currentTimeMillis() - 24 * 60 * 60 * 1000);
            String tagString = profileMapper.tags(username);
            JSONArray tags = JSONObject.parseArray(tagString);
            JSONObject tiredTag = new JSONObject();
            tiredTag.put("result", enumSet.getTiredEasily());
            tiredTag.put("proposal", tagsMapper.getProposal(enumSet.getTiredEasily()));
            if ((tiredTimeOfDay > 1) && !tags.contains(tiredTag)) {
                // 更新易疲劳标签
                if (!tags.contains(tiredTag)) {
                    tags.add(tiredTag);
                    tagsUpdated = true;
//					profileMapper.addTag(username, tags.toJSONString());
                }
            } else if (tags.contains(tiredTag)) {
                // 将以前易疲劳标签删掉
                tags.remove(tiredTag);
                tagsUpdated = true;
//				profileMapper.addTag(username, tags.toJSONString());
            }
            JSONObject tagBrHigh = new JSONObject();
            tagBrHigh.put("result", enumSet.getBrHigh());
            tagBrHigh.put("proposal", tagsMapper.getProposal(enumSet.getBrHigh()));
            JSONObject tagBrLow = new JSONObject();
            tagBrLow.put("result", enumSet.getBrLow());
            tagBrLow.put("proposal", tagsMapper.getProposal(enumSet.getBrLow()));
            JSONObject tagHrHigh = new JSONObject();
            tagHrHigh.put("result", enumSet.getHrHigh());
            tagHrHigh.put("proposal", tagsMapper.getProposal(enumSet.getHrHigh()));
            JSONObject tagHrLow = new JSONObject();
            tagHrLow.put("result", enumSet.getHrLow());
            tagHrLow.put("proposal", tagsMapper.getProposal(enumSet.getHrLow()));
            if (tags.contains(tagHrLow) || tags.contains(tagHrHigh) || tags.contains(tagBrHigh)
                    || tags.contains(tagBrLow)) {
                tags.remove(tagHrLow);
                tags.remove(tagHrHigh);
                tags.remove(tagBrHigh);
                tags.remove(tagBrLow);
                tagsUpdated = true;
            }
            int maxBr = Math.max(brHigh, brLow);
            int maxHr = Math.max(hrHigh, hrLow);
            if (maxBr > 1) {
                if (maxBr == brHigh) {
                    tags.add(tagBrHigh);
                } else {
                    tags.add(tagBrLow);
                }
                tagsUpdated = true;
            }
            if (maxHr > 1) {
                if (maxHr == hrHigh) {
                    tags.add(tagHrHigh);
                } else {
                    tags.add(tagHrLow);
                }
                tagsUpdated = true;
            }
            if (tagsUpdated)
                profileMapper.addTag(username, tags.toJSONString());
        }
    }

    @Override
    public List<JSONObject> historyIVI(String username) {
        return deviceMapper.historyIVI(username);
    }

    @Override
    @Transactional
    public void removeHistoryIVI(String username, Long updatetime, Integer[] set) {
        deviceMapper.removeHistoryIVI(set);
        deviceMapper.rollbackIVI(username, updatetime);
        int c=deviceMapper.iviHistoryCount(username);
        if(c<1){
            deviceMapper.resetIvi(username);
        }
        updateRelatedWithIvi(username);
    }

    private void updateBpScoreAndTag(String username) {
        // 收集过去48小时内的最新最多10条测量记录
        List<JSONObject> recentSet = scoreMapper.collectRecentOfBp(username,
                System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000);
        if (recentSet.size() > 0) {// 48小时内有测量记录
            int severe = 0, medium = 0, little = 0, terminate = 0, size = 0;
            for (JSONObject r : recentSet) {
                size+=r.getIntValue("count");
                if (enumSet.getBpSevere().equals(r.getString("status")))
                    severe = r.getIntValue("count");
                else if (enumSet.getBpMedium().equals(r.getString("status")))
                    medium = r.getIntValue("count");
                else if (enumSet.getBpLittle().equals(r.getString("status")))
                    little = r.getIntValue("count");
                else if (enumSet.getBpTerminate().equals(r.getString("status")))
                    terminate = r.getIntValue("count");
            }
//            size = terminate + little + medium + severe;
            int score = 100;
            if (size > 0) {
                score = 100 - (10 / size * 4) * terminate - (10 / size * 6) * little - (10 / size * 8) * medium
                        - (100 / size) * severe;
            }
            // 更新血压分数
            scoreMapper.upgradeBp(username, score);
            JSONArray tags = JSONObject.parseArray(profileMapper.tags(username));
            JSONObject tagTerminate = new JSONObject(), tagLittle = new JSONObject(), tagMedium = new JSONObject(),
                    tagSevere = new JSONObject();
            tagTerminate.put("result", enumSet.getBpTerminate());
            tagTerminate.put("proposal", tagsMapper.getProposal(enumSet.getBpTerminate()));
            tagLittle.put("result", enumSet.getBpLittle());
            tagLittle.put("proposal", tagsMapper.getProposal(enumSet.getBpLittle()));
            tagMedium.put("result", enumSet.getBpMedium());
            tagMedium.put("proposal", tagsMapper.getProposal(enumSet.getBpMedium()));
            tagSevere.put("result", enumSet.getBpSevere());
            tagSevere.put("proposal", tagsMapper.getProposal(enumSet.getBpSevere()));
            // 根据血压状态更新用户健康标签
            if (tags.contains(tagSevere))
                tags.remove(tagSevere);
            if (tags.contains(tagMedium))
                tags.remove(tagMedium);
            if (tags.contains(tagLittle))
                tags.remove(tagLittle);
            if (tags.contains(tagTerminate))
                tags.remove(tagTerminate);
            if (score < 100) {
                int max = Math.max(terminate, Math.max(little, Math.max(medium, severe)));
                if (max > 1) {
                    String status = enumSet.getNormal();
                    if (max == severe)
                        status = enumSet.getBpSevere();
                    else if (max == medium)
                        status = enumSet.getBpMedium();
                    else if (max == little)
                        status = enumSet.getBpLittle();
                    else
                        status = enumSet.getBpTerminate();
                    JSONObject tag = new JSONObject();
                    tag.put("result", status);
                    tag.put("proposal", tagsMapper.getProposal(status));
                    tags.add(tag);
                }
            }
            profileMapper.addTag(username, tags.toJSONString());
        }
    }
}
