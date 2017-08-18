/*
 * 订单受理接口
 * qht_financeorder
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sysconst.Code;
import com.util.Dao;
import com.util.DateUtil;

import qht.game.node.CountOrderNode;
import qht.game.node.FinanceAMDOWNOrderNode;
import qht.game.node.FinanceAMUPOrderNode;
import qht.game.node.FinanceOrderNode;

public class FinanceOrderDao {

	public boolean insert(FinanceOrderNode record) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", record.getId());
		parameter.put("odd", record.getOdd());
		parameter.put("username", record.getUsername());
		parameter.put("type", record.getType());
		parameter.put("amount", record.getAmount());
		parameter.put("requesttime", record.getRequesttime());
		parameter.put("requestname", record.getRequestname());
		parameter.put("tag", record.getTag());
		parameter.put("updatetime", record.getUpdatetime());
		if (1!=Dao.insert("financeorderInsertOne", parameter))
			return false;
		return true;		
	}

	public FinanceOrderNode selectOne(long id) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		return (FinanceOrderNode) Dao.selectOne("financeorderSelectOne", parameter);
	}

	public boolean setTag(long id, int tagIgnore) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("tag", tagIgnore);
		parameter.put("updatetime", DateUtil.getCurTimestamp().getTime());
		if (1!=Dao.update("financeorderSetTag",parameter))
			return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<FinanceOrderNode> selectListup(Map<String, Object> parameter) {
		return (List<FinanceOrderNode>) Dao.selectList("upOrderSelectList", parameter);
	}
	@SuppressWarnings("unchecked")
	public List<FinanceOrderNode> selectListdown(Map<String, Object> parameter) {
		return (List<FinanceOrderNode>) Dao.selectList("downOrderSelectList", parameter);
	}
	public FinanceAMUPOrderNode countByAMUP(String username,int tag,long lbegintime,long lendtime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("tag", tag);
		parameter.put("begintime", lbegintime);
		parameter.put("endtime", lendtime);
		return (FinanceAMUPOrderNode) Dao.selectOne("countByAMUP", parameter);
	}
	public FinanceAMDOWNOrderNode countByAMDOWN(String username,int tag,long lbegintime,long lendtime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("username", username);
		parameter.put("tag", tag);
		parameter.put("begintime", lbegintime);
		parameter.put("endtime", lendtime);
		return (FinanceAMDOWNOrderNode) Dao.selectOne("countByAMDOWN", parameter);
	}

	@SuppressWarnings("unchecked")
	public List<FinanceOrderNode> selectList() {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("tag", Code.TAG_NODEAL);
		return (List<FinanceOrderNode>) Dao.selectList("financeorderSelectList", parameter);
	}
	
	public CountOrderNode countselectList() {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("tag", Code.TAG_NODEAL);
		return (CountOrderNode) Dao.selectOne("COUNTorderSelectList", parameter);
	}
}
