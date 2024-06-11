/**
 * 
 */
package com.neusoft.rnma.ichpoc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author liangyf
 *
 */
@Mapper
public interface TagsMapper {

	List<JSONObject> all();

	String getProposal(String name);

}
