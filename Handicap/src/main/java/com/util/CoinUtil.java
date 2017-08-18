/**
 * BigDecimal 类型封装
 * @author kinphy
 * @Time 2017-02-13 16:54:21
 */

package com.util;

import java.math.BigDecimal;

public class CoinUtil {
	
	private final static int DEF_DIV_SCALE = 2;
	
	public static BigDecimal zero = new BigDecimal("0.00");
	
	public static BigDecimal add(BigDecimal b1,BigDecimal b2) {
		if (b1==null || b2==null)
			return null;
		BigDecimal result = b1.add(b2);
		return result.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal sub(BigDecimal b1,BigDecimal b2) {
		if (b1==null || b2==null)
			return null;
		BigDecimal result = b1.subtract(b2);
		return result.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal mul(BigDecimal b1,BigDecimal b2) {
		if (b1==null || b2==null)
			return null;
		BigDecimal result = b1.multiply(b2);
		return result.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal divide(BigDecimal b1,BigDecimal b2) {
		if (b1==null || b2==null)
			return null;
		return b1.divide(b2,DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP);
	}
	
	public static int compare(BigDecimal b1,BigDecimal b2) {
		if (b1==null || b2==null)
			return 0;
		return b1.compareTo(b2);
	}
	
	public static int compareZero(BigDecimal b) {
		if (b==null)
			return 0;
		return b.compareTo(zero);
	}
	
	public static BigDecimal reverse(BigDecimal b) {
		if (b==null)
			return null;
		BigDecimal temp = new BigDecimal("-1");
		return mul(b,temp);
	}
	
	public static BigDecimal mulInt(BigDecimal d, int i) {
		BigDecimal dd = new BigDecimal(i);
		return mul(d, dd);
	}
	
	public static BigDecimal divideInt(BigDecimal d, int i) {
		BigDecimal dd = new BigDecimal(i);
		return divide(d, dd);
	}
	
	public static BigDecimal createNew(double d) {
		BigDecimal result = new BigDecimal(d);
		return result.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal createNew(String s) {
		BigDecimal result = new BigDecimal(s);
		return result.setScale(DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
	}
	
	
	
	public static BigDecimal add(BigDecimal b1,BigDecimal b2, int scale) {
		if (b1==null || b2==null)
			return null;
		BigDecimal result = b1.add(b2);
		return result.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal sub(BigDecimal b1,BigDecimal b2, int scale) {
		if (b1==null || b2==null)
			return null;
		BigDecimal result = b1.subtract(b2);
		return result.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal mul(BigDecimal b1,BigDecimal b2, int scale) {
		if (b1==null || b2==null)
			return null;
		BigDecimal result = b1.multiply(b2);
		return result.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal divide(BigDecimal b1,BigDecimal b2, int scale) {
		if (b1==null || b2==null)
			return null;
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal reverse(BigDecimal b, int scale) {
		if (b==null)
			return null;
		BigDecimal temp = new BigDecimal("-1");
		return mul(b,temp, scale);
	}
	
	public static BigDecimal mulInt(BigDecimal d, int i, int scale) {
		BigDecimal dd = new BigDecimal(i);
		return mul(d, dd, scale);
	}
	
	public static BigDecimal divideInt_v(BigDecimal d, int i, int scale) {
		BigDecimal dd = new BigDecimal(i);
		return divide(d, dd, scale);
	}
	
	public static BigDecimal createNew(double d, int scale) {
		BigDecimal result = new BigDecimal(d);
		return result.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal createNew(String s, int scale) {
		BigDecimal result = new BigDecimal(s);
		return result.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal Round(BigDecimal b) {
		return b.setScale(0, BigDecimal.ROUND_HALF_UP);
	}
}
