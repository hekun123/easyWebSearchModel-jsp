package cn.java.ir.action;

import java.util.Comparator;

import cn.java.ir.db.news_info;

public class SortByTime implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		news_info a = (news_info)o1;
		news_info b = (news_info)o2;
		
		return b.getDate().compareTo(a.getDate());
	}

}
