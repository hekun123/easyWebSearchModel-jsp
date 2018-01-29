package cn.java.ir.lucence;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.java.ir.action.Searcher;
import cn.java.ir.db.news_info;

public class TestSearch {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		int curPageIndex = 2; 
		String queryStr = "贺坤";
		queryStr = new String(queryStr.getBytes("iso8859-1"), "UTF-8");
		String indexPath = "./WebContent/index";
		List<news_info>results = Searcher.search(indexPath, queryStr, 10, curPageIndex, null);
		for(int i = 0 ; i < results.size() ; i++) {
			news_info news = results.get(i);
			System.out.println(news.getTitle());
		}
	}

}
