package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.Pk10WebinfoNode;

public class Pk10WebinfoDao {

	@SuppressWarnings("unchecked")
	public List<Pk10WebinfoNode> selectList() {
		return (List<Pk10WebinfoNode>) Dao.selectList("pk10webinfoselectlist", null);
	}

	public boolean edit(int id, String url, String username, String password, long updatetime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("url", url);
		parameter.put("username", username);
		parameter.put("password", password);
		parameter.put("updatetime", updatetime);
		if (1!=Dao.update("pk10webinfoupdate",parameter))
			return false;
		return true;
	}

	public boolean setStatus(int id, int status, long updatetime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("status", status);
		parameter.put("updatetime", updatetime);
		if (1!=Dao.update("pk10webinfosetstatus",parameter))
			return false;
		return true;
	}

	public boolean updateInit(int status) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("status", status);
		Dao.update("pk10webinfosetstatusinit",parameter);
		return true;
	}

}
