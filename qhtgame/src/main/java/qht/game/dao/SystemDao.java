/*
 * 全局系统接口
 * qht_system
 */

package qht.game.dao;

import java.util.HashMap;
import java.util.Map;

import com.util.Dao;

public class SystemDao {

	public String selectOne(int id) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		return (String) Dao.selectOne("systemSelectOne", parameter);
	}

	public boolean update(int id, String str) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("data", str);
		if (1!=Dao.update("systemUpdate",parameter))
			return false;
		return true;
	}

}
