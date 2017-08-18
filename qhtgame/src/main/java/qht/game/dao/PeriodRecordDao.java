/*
 * 期数记录接口
 * qht_periodrecord
 */
package qht.game.dao;

import com.util.Dao;

import qht.game.node.PeriodRecord;

public class PeriodRecordDao{
	public boolean insert(PeriodRecord record) {
		if (1!=Dao.insert("periodrecordInsert",record.getParameter()))
			return false;
		return true;
	}
}
