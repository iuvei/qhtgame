package com.node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapData implements Map<Object, Object>{

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	protected Map<Object, Object> data;
	
	public MapData() {
		data = new HashMap<Object, Object>();
	}
	
	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		return data.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set entrySet() {
		return data.entrySet();
	}

	@Override
	public Object get(Object arg0) {
		return data.get(arg0);
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set keySet() {
		return data.keySet();
	}

	@Override
	public Object put(Object arg0, Object arg1) {
		return data.put(arg0, arg1);
	}

	@Override
	public void putAll(Map<?, ?> arg0) {
		data.putAll(arg0);
	}

	@Override
	public Object remove(Object arg0) {
		return data.remove(arg0);
	}

	@Override
	public int size() {
		return data.size();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection values() {
		return data.values();
	}

}
