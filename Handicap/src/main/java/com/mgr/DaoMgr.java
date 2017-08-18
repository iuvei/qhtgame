package com.mgr;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import com.util.Dao;

import qht.game.dao.Pk10HandicapDao;
import qht.game.dao.Pk10WebinfoDao;

public class DaoMgr {
	private static final int DAO_PK10HANDICAP = 1;
	private static final int DAO_PK10WEBINFO = 2;
	
	private static Map<Integer,Object> mdata = new HashMap<Integer,Object>();
	
	public static void init(SqlSessionTemplate sqlSessionTemplate) {
		Dao.init(sqlSessionTemplate);
		mdata.put(DAO_PK10HANDICAP, new Pk10HandicapDao());
		mdata.put(DAO_PK10WEBINFO, new Pk10WebinfoDao());
	}

	public static Pk10HandicapDao getPk10Handicap() {
		return (Pk10HandicapDao) mdata.get(DAO_PK10HANDICAP);
	}

	public static Pk10WebinfoDao getPk10Webinfo() {
		return (Pk10WebinfoDao) mdata.get(DAO_PK10WEBINFO);
	}
}
