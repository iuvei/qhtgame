/*
 * 金额变动接口
 * qht_amountrecord
 */
package qht.game.dao;

import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.AmountRecord;

public class AmountRecordDao {

	public boolean insert(AmountRecord record) {
		if (1!=Dao.insert("amountrecordInsert",record.getParameter()))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<AmountRecord> selectList(Map<String, Object> parameter) {
		return (List<AmountRecord>) Dao.selectList("amountrecordSelectList", parameter);
	}

}
