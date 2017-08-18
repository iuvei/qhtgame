package com.pk10.logic;

import java.util.List;


import com.node.ErrorCode;
import com.node.Pk10CurNode;
import com.node.Pk10Info;

public interface Pk10WebInter {
	public static final String CTYPEP2 = "ctypep2";	//冠、亚 组合
	public static final String CTYPEP3 = "ctypep3";	//三四五六
	public static final String CTYPEP4 = "ctypep4";	//七八九十
	
	public int getId();
	public boolean isOk();
	public Pk10CurNode getInfo();
	public boolean open1(String url,String username,String password);
	public boolean open2(String path);
	public boolean open3(String check);
	public void close();
	public boolean flushInfo();
	public ErrorCode bet(String ctypep,String period,List<Pk10Info> data);

}
