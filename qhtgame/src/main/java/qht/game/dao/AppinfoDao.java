/*
 * APP信息接口
 * qht_appinfo
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.AppinfoNode;

public class AppinfoDao {

	@SuppressWarnings("unchecked")
	public List<AppinfoNode> selectList(String appcode) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", appcode);
		return (List<AppinfoNode>) Dao.selectList("appinfoSelectList", parameter);
	}

	public boolean setTime(String appcode,long actiontime,long updatetime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", appcode);
		parameter.put("actiontime", actiontime);
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("appinfoSetTime",parameter))
			return false;
		return true;
	}
	
	public boolean editWC(String appcode,String wechat_p,String wechat_code,long updatetime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", appcode);
		parameter.put("wechat_p", wechat_p);
		parameter.put("wechat_code", wechat_code);
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("appinfoEditwc",parameter))
			return false;
		return true;
	}

	public boolean insert(Map<String, Object> parameter) {
		if (1!=Dao.insert("appinfoInsertOne", parameter))
			return false;
		return true;
	}

	public AppinfoNode selectOne(String appcode) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("appcode", appcode);
		return (AppinfoNode) Dao.selectOne("appinfoSelectOne", parameter);
	}

}
