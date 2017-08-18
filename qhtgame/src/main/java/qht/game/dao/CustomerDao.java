/*
 * IM账号接口
 * qht_customer
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.CustomerNode;

public class CustomerDao {

	@SuppressWarnings("unchecked")
	public List<CustomerNode> selectList(String cus_num,int cus_id) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("cus_num", cus_num);
		parameter.put("id", cus_id);
		return (List<CustomerNode>) Dao.selectList("customerSelectList", parameter);
	}

	public boolean editCUS(String cus_num,String cus_pwd,String cus_name,long updatetime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("cus_num", cus_num);
		parameter.put("cus_pwd", cus_pwd);
		parameter.put("cus_name", cus_name);
		parameter.put("updatetime", updatetime);
		
		if (1!=Dao.update("customerEditcus",parameter))
			return false;
		return true;
	}

	public CustomerNode selectOne(String username) {
		Map<String, Object> parameter = new HashMap<String,Object>();
		parameter.put("cus_num", username);
		return (CustomerNode) Dao.selectOne("customerSelectOne", parameter);
	}
}
