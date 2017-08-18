/*
 * 随机工具类
 */

package com.util;

import java.util.ArrayList;
import java.util.Random;

public class RandUtil {
	
	private static Random random = new Random();
	
	/*
	 * 随机获取一个boolean值
	 * */
	public static boolean getBoolean() {
		return random.nextBoolean();
	}
	
	/*
	 * 随机获取一个int数值
	 * */
	public static int randInt() {
		return random.nextInt();
	}
		
	/*
	 * 在[min-max]之间随机获取一个值
	 * */
	public static int randInt(int min, int max) {
		return min+random.nextInt(max-min+1);
	}
	
	/*
	 * 在[min-max]之间随机获取m个值
	 * */
	public static ArrayList<Integer> randInts(int min, int max, int m) {

		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int i=min; i<=max; i++) {
			arr.add(i);
		}
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i=0; i<m; i++) {
			int index = random.nextInt(arr.size());
			int value = arr.get(index);
			arr.remove(index);
			result.add(value);
		}
		return result;
	}
	
	/*
	 * 在[min-max]之间随机获取m个值
	 * */
	public static int[] randIntss(int min, int max, int m) {

		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int i=min; i<=max; i++) {
			arr.add(i);
		}
		
		int[] result = new int[m];
		for (int i=0; i<m; i++) {
			int index = random.nextInt(arr.size());
			result[i] = arr.get(index);
			arr.remove(index);
		}
		return result;
	}
	
	/*
	 * 打乱[min-max]之间数值
	 * */
	public static ArrayList<Integer> randIntss(int min, int max) {
		int nNum = max-min+1;
		if(nNum<=0)
			return null;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i=0; i<nNum; i++) {
			arr.add(min+i);
		}
		
		nNum = arr.size();
		ArrayList<Integer> arrResult = new ArrayList<Integer>();
		int rand;
		for (int i=0; i<nNum; i++) {
			rand = random.nextInt(arr.size());
			arrResult.add(arr.get(rand));
			arr.remove(rand);
		}
		
		return arrResult;
	}
	
	/*
	 * 打乱[min-max]之间数值
	 * */
	public static int[] randInts(int min, int max) {
		int nNum = max-min+1;
		if(nNum<=0)
			return null;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i=0; i<nNum; i++) {
			arr.add(min+i);
		}
		
		nNum = arr.size();
		int[] arrResult = new int[nNum];
		int rand;
		for (int i=0; i<nNum; i++) {
			rand = random.nextInt(arr.size());
			arrResult[i] = arr.get(rand);
			arr.remove(rand);
		}
		
		return arrResult;
	}
	
	/*
	 * 打乱[min-max]之间除arrBut中数字的数值
	 * */
	public static int[] randIntsBut(int min, int max, int[] arrBut) {
		
		int nNum = max-min+1;
		if(nNum<=0 || arrBut.length>nNum)
			return null;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		boolean b = true;
		for(int i=0; i<nNum; i++) {
			b = true;
			for(int j=0; j<arrBut.length; j++) {
				if (arrBut[j]==min+i) {
					b = false;
					break;
				}
			}
			
			if (b)
				arr.add(min+i);
		}
		
		nNum = arr.size();
		int[] arrResult = new int[nNum];
		int rand;
		for (int i=0; i<nNum; i++) {
			rand = random.nextInt(arr.size());
			arrResult[i] = arr.get(rand);
			arr.remove(rand);
		}
		
		return arrResult;
	}
	
	/*
	 * 打乱[min-max]之间除arrBut中数字的数值
	 * */
	public static int[] randIntsBut(int min, int max, ArrayList<Integer> arrBut) {

		int nNum = max-min+1;
		if(nNum<=0 || arrBut.size()>nNum)
			return null;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		boolean b = true;
		for(int i=0; i<nNum; i++) {
			b = true;
			for(int j=0; j<arrBut.size(); j++) {
				if (arrBut.get(j)==min+i) {
					b = false;
					break;
				}
			}
			
			if (b)
				arr.add(min+i);
		}
		
		nNum = arr.size();
		int[] arrResult = new int[nNum];
		int rand;
		for (int i=0; i<nNum; i++) {
			rand = random.nextInt(arr.size());
			arrResult[i] = arr.get(rand);
			arr.remove(rand);
		}
		
		return arrResult;
	}
	
	/*
	 * 从[min-max]之间除arrBut中数字随机选取m个
	 * */
	public static ArrayList<Integer> randIntssBut(int min, int max, int m, ArrayList<Integer> arrBut) {

		int nNum = max-min+1;
		if(nNum<=0 || arrBut.size()>nNum)
			return null;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		boolean b = true;
		for(int i=0; i<nNum; i++) {
			b = true;
			for(int j=0; j<arrBut.size(); j++) {
				if (arrBut.get(j)==min+i) {
					b = false;
					break;
				}
			}
			
			if (b)
				arr.add(min+i);
		}
		
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i=0; i<m; i++) {
			int index = random.nextInt(arr.size());
			result.add(arr.get(index));
			arr.remove(index);
		}
		
		return result;
	}
}
