/*
 * 后台管理员信息表
 * qht_admin
 */
package qht.game.dao;

import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.AdminNode;

public class AdminDao{

	public AdminNode selectOne(Map<String, Object> parameter) {
		return (AdminNode) Dao.selectOne("adminSelectOne", parameter);
	}
	
	@SuppressWarnings("unchecked")
	public List<AdminNode> selectList(Map<String, Object> parameter) {
		return (List<AdminNode>) Dao.selectList("adminSelectList", parameter);
	}

	public int insert(Map<String, Object> parameter) {
		return Dao.insert("adminInsertOne",parameter);
	}

	public boolean setStatus(Map<String, Object> parameter) {
		if (1!=Dao.update("adminSetStatus",parameter))
			return false;
		return true;
	}

	public boolean resetPassword(Map<String, Object> parameter) {
		if (1!=Dao.update("adminResetPassword",parameter))
			return false;
		return true;
	}

	public boolean editNickname(Map<String, Object> parameter) {
		if (1!=Dao.update("adminEditNickname",parameter))
			return false;
		return true;
	}
}
