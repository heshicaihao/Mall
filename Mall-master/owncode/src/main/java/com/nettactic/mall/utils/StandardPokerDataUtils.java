package com.nettactic.mall.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nettactic.mall.R;

public class StandardPokerDataUtils {
	
	private static List<Integer> mDatas02 = new ArrayList<Integer>(
			Arrays.asList(R.mipmap.p001, R.mipmap.p002));

	private static List<Integer> mDatas04 = new ArrayList<Integer>(
			Arrays.asList(R.mipmap.p001, R.mipmap.p002, R.mipmap.p003,
					R.mipmap.p011));

	private static List<Integer> mDatas16 = new ArrayList<Integer>(
			Arrays.asList(R.mipmap.p001, R.mipmap.p002, R.mipmap.p003,
					R.mipmap.p011, R.mipmap.p021, R.mipmap.p031,
					R.mipmap.p041, R.mipmap.p051, R.mipmap.p061,
					R.mipmap.p071, R.mipmap.p081, R.mipmap.p091,
					R.mipmap.p101, R.mipmap.p111, R.mipmap.p121,
					R.mipmap.p131));

	private static List<Integer> mDatas55 = new ArrayList<Integer>(Arrays.asList(
			R.mipmap.p001, R.mipmap.p002, R.mipmap.p003, R.mipmap.p011,
			R.mipmap.p012, R.mipmap.p013, R.mipmap.p014, R.mipmap.p021,
			R.mipmap.p022, R.mipmap.p023, R.mipmap.p024, R.mipmap.p031,
			R.mipmap.p032, R.mipmap.p033, R.mipmap.p034, R.mipmap.p041,
			R.mipmap.p042, R.mipmap.p043, R.mipmap.p044, R.mipmap.p051,
			R.mipmap.p052, R.mipmap.p053, R.mipmap.p054, R.mipmap.p061,
			R.mipmap.p062, R.mipmap.p063, R.mipmap.p064, R.mipmap.p071,
			R.mipmap.p072, R.mipmap.p073, R.mipmap.p074, R.mipmap.p081,
			R.mipmap.p082, R.mipmap.p083, R.mipmap.p084, R.mipmap.p091,
			R.mipmap.p092, R.mipmap.p093, R.mipmap.p094, R.mipmap.p101,
			R.mipmap.p102, R.mipmap.p103, R.mipmap.p104, R.mipmap.p111,
			R.mipmap.p112, R.mipmap.p113, R.mipmap.p114, R.mipmap.p121,
			R.mipmap.p122, R.mipmap.p123, R.mipmap.p124, R.mipmap.p131,
			R.mipmap.p132, R.mipmap.p133, R.mipmap.p134));

	public static List<Integer> getData(String page) {
		int pageInt = Integer.parseInt(page);
		List<Integer> data = new ArrayList<Integer>();
		List<Integer> getmDatas55 = mDatas55;
		if (page.equals("2")) {
			data = mDatas02;
		}else if(page.equals("4")){
			data = mDatas04;
		}else if(page.equals("16")){
			data = mDatas16;
		}else if(page.equals("55")){
			data = mDatas55;
		}else {
			for (int i = 0; i < getmDatas55.size(); i++) {
				Integer integer = getmDatas55.get(i);
				if (i < pageInt) {
					data.add(integer);
				}
			}
		}
		return data;
	}

}
