/*
 * pk10分隔符号符接口
 * qht_pk10_symbol
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.Pk10SymbolNode;

public class Pk10SymbolDao {

	@SuppressWarnings("unchecked")
	public List<Pk10SymbolNode> selectList() {
		return (List<Pk10SymbolNode>) Dao.selectList("pk10symbolSelectList", null);
	}

	public boolean updateTag(int id, int tag, long updatetime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("id", id);
		parameter.put("tag", tag);
		parameter.put("updatetime", updatetime);
		if (1!=Dao.update("pk10symbolUpdate",parameter))
			return false;
		return true;
	}

}
