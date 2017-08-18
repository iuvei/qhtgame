/*
 * 财务记录接口
 * qht_financerecord
 */
package qht.game.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.Dao;

import qht.game.node.FinanceRecord;
import qht.game.node.FinanceReportNode;

public class FinanceRecordDao {

	public boolean insert(FinanceRecord record) {
		if (1!=Dao.insert("financerecordInsert",record.getParameter()))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<FinanceRecord> selectList(Map<String, Object> parameter) {
		return (List<FinanceRecord>) Dao.selectList("financerecordSelectList", parameter);
	}

	@SuppressWarnings("unchecked")
	public List<FinanceReportNode> sumSelectList(long beginTime, long endTime) {
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("begintime", beginTime);
		parameter.put("endtime", endTime);
		return (List<FinanceReportNode>) Dao.selectList("financereportSelectList", parameter);
	}
}
