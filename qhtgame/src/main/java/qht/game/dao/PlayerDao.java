/*
 * 玩家信息接口
 * qht_player
 */
package qht.game.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;
import com.util.DateUtil;

import qht.game.node.PlayerAMOrderNode;
import qht.game.node.PlayerNode;

public class PlayerDao{
	
	public PlayerNode selectOne(Map<String, Object> parameter) {
		return (PlayerNode) Dao.selectOne("playerSelectOne", parameter);
	}
	
	public PlayerNode selectOne2(Map<String, Object> parameter) {
		return (PlayerNode) Dao.selectOne("playerSelectOne2", parameter);
	}
	
	public PlayerNode selectOneByName(String username) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		return (PlayerNode) Dao.selectOne("playerbynameSelectOne", parameter);
	}

	@SuppressWarnings("unchecked")
	public List<PlayerNode> selectList(Map<String, Object> parameter) {
		return (List<PlayerNode>) Dao.selectList("playerSelectList", parameter);
	}
	
	public PlayerAMOrderNode countByAMplayer() {
		return (PlayerAMOrderNode) Dao.selectOne("countByAMplayer", null);
	}
	
	public boolean updateBalance(int id, BigDecimal balance, long updatetime) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("id", id);
		parameter.put("balance", balance);
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("playerUpdateBalance",parameter))
			return false;
		
		return true;
	}

	public boolean insert(Map<String, Object> parameter) {
		if (1!=Dao.insert("playerInsertOne", parameter))
			return false;
		return true;
	}

	public boolean setStatus(Map<String, Object> parameter) {
		if (1!=Dao.update("playerSetStatus",parameter))
			return false;
		return true;
	}

	public boolean resetPassword(Map<String, Object> parameter) {
		if (1!=Dao.update("playerResetPassword",parameter))
			return false;
		return true;
	}
	
    public boolean resetPsw(Map<String, Object> parameter) {
        if (1 != Dao.update("playerResetPsw", parameter))
            return false;
        return true;
    }

	public boolean editInfo(Map<String, Object> parameter) {
		if (1!=Dao.update("playerEditInfo",parameter))
			return false;
		return true;
	}
	
    public boolean isExistByTel(String tel) {
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("tel", tel);
        PlayerNode playerNode = (PlayerNode) Dao.selectOne("playerbytelSelectOne", parameter);
        if (playerNode == null) {
            return false;
        }else {
            return true;
        }
    }

	public PlayerNode selectByLoginname(String loginname) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("loginname", loginname);
		return (PlayerNode) Dao.selectOne("playerbyloginnameSelectOne", parameter);
	}

	public boolean setTypeid(int id, int typeid) {
		long updatetime = DateUtil.getCurTimestamp().getTime();
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("typeid", typeid);
		parameter.put("updatetime", updatetime);
		if (1!=Dao.update("playersettypeid",parameter))
			return false;
		return true;
	}
}
