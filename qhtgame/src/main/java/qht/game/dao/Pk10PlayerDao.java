/*
 * pk10机器人信息接口
 * qht_pk10_player
 */
package qht.game.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.util.Dao;

import qht.game.node.Pk10PlayerNode;
import qht.game.node.Pk10PlayerNodeDB;
import qht.game.node.RebotSchemeNode;

public class Pk10PlayerDao {
	
	@SuppressWarnings("unchecked")
	public List<Pk10PlayerNodeDB> selectList() {
		return (List<Pk10PlayerNodeDB>) Dao.selectList("pk10playerSelectList", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Pk10PlayerNodeDB> selectList2(String nickname, int status) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("nickname", nickname);
		parameter.put("status", status);
		return (List<Pk10PlayerNodeDB>) Dao.selectList("pk10playerSelectList2", parameter);
	}

	public boolean insert(Pk10PlayerNode info) {
		String schemes = new Gson().toJson(info.getSchemes());
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("appcode", info.getAppcode());
		parameter.put("username", info.getUsername());
		parameter.put("nickname", info.getNickname());
		parameter.put("status", info.getStatus());
		parameter.put("balance", info.getBalance());
		parameter.put("schemes", schemes);
		parameter.put("createtime", info.getCreatetime());
		parameter.put("updatetime", info.getUpdatetime());
		
		if (1!=Dao.insert("pk10playerInsert",parameter))
			return false;
		
		return true;
	}

	public Pk10PlayerNodeDB selectOne(String username) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		return (Pk10PlayerNodeDB) Dao.selectOne("pk10playerSelectOne", parameter);
	}

	public boolean update(String username, String nickname, int status, long updatetime) {

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("username", username);
		parameter.put("nickname", nickname);
		parameter.put("status", status);
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("pk10playerUpdate",parameter))
			return false;
		
		return true;
	}

	public boolean update2(String username, List<RebotSchemeNode> schemes, long updatetime) {
		String text_schemes = new Gson().toJson(schemes);
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("username", username);
		parameter.put("schemes", text_schemes.getBytes());
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("pk10playerUpdate2",parameter))
			return false;
		
		return true;
	}

	public boolean updateBalance(String username, BigDecimal balance, long updatetime) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("username", username);
		parameter.put("balance", balance);
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("pk10playerUpdateBalance",parameter))
			return false;
		return true;
		
	}
}
/*
	public boolean updateBalance(int id, BigDecimal balance, long updatetime) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("id", id);
		parameter.put("balance", balance);
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("pk10playerUpdateBalance",parameter))
			return false;
		return true;
	}
	
	public boolean edit(int id,String appcode, String username, BigDecimal balance) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("id", id);
		parameter.put("appcode", appcode);
		parameter.put("username", username);
		parameter.put("balance", balance);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		if (1!=Dao.update("pk10robotupdateone", parameter))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Pk10PlayerNode> selectList() {
		return (List<Pk10PlayerNode>) Dao.selectList("pk10playerSelectList", null);
	}

	public boolean insert(Pk10PlayerNode player) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("appcode", player.getAppcode());
		parameter.put("username", player.getUsername());
		parameter.put("balance", player.getBalance());
		parameter.put("createtime", player.getCreatetime());
		parameter.put("updatetime", player.getUpdatetime());
		
		if (1!=Dao.insert("pk10playerInsertOne", parameter))
			return false;
		return true;
	}

	public Pk10PlayerNode selectOne(String username) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		return (Pk10PlayerNode) Dao.selectOne("pk10playerSelectOne", parameter);
	}

	public boolean removeOne(int id) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		if (1!=Dao.delete("pk10playerDeleteOne", parameter))
			return false;
		return true;
	}
	*/

