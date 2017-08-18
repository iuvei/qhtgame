/*
 * pk10机器人方案接口
 * qht_robot_scheme
 */
package qht.game.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.pk10.node.Pk10RebotScheme;
import com.util.Dao;

import qht.game.node.Pk10RebotSchemeDB;

public class RebotSchemeDao {

	@SuppressWarnings("unchecked")
	public List<Pk10RebotSchemeDB> selectList() {
		return (List<Pk10RebotSchemeDB>) Dao.selectList("rebotschemeSelectList", null);
	}

	public boolean insert(Pk10RebotScheme info) {
		String text = new Gson().toJson(info.getSend_text());
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("name", info.getName());
		parameter.put("low_amount", info.getLow_amount());
		parameter.put("sendup_amount", info.getSendup_amount());
		parameter.put("up_amount", info.getUp_amount());
		parameter.put("senddown_amount", info.getSenddown_amount());
		parameter.put("stop_amount", info.getStop_amount());
		parameter.put("send_text", text);
		parameter.put("createtime", info.getCreatetime());
		parameter.put("updatetime", info.getUpdatetime());
		
		if (1!=Dao.insert("rebotschemeInsert",parameter))
			return false;
		
		return true;
	}

	public Pk10RebotSchemeDB selectOne(String name) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("name", name);
		return (Pk10RebotSchemeDB) Dao.selectOne("rebotschemeSelectOne", parameter);
	}

	public boolean update1(String name, BigDecimal low_amount, BigDecimal sendup_amount, BigDecimal up_amount,
			BigDecimal senddown_amount, BigDecimal stop_amount, long updatetime) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("name", name);
		parameter.put("low_amount", low_amount);
		parameter.put("sendup_amount", sendup_amount);
		parameter.put("up_amount", up_amount);
		parameter.put("senddown_amount", senddown_amount);
		parameter.put("stop_amount", stop_amount);
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("rebotschemeUpdate1",parameter))
			return false;
		
		return true;
	}

	public boolean update2(String name, List<String> arrText, long updatetime) {
		String sendText = new Gson().toJson(arrText);
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("name", name);
		parameter.put("send_text", sendText.getBytes());
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("rebotschemeUpdate2",parameter))
			return false;
		
		return true;
	}

}
