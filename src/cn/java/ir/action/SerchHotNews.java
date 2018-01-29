package cn.java.ir.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.google.gson.Gson;

import cn.java.ir.db.news_info;

/**
 * Servlet implementation class SerchHotNews
 */
//@WebServlet("/SerchHotNews")
public class SerchHotNews extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		String queryStr = request.getParameter("queryStr");
		String sortMethod = request.getParameter("sortnews");
		//System.out.println("queryStr:" + queryStr + "\t" + "sortMethod:" + sortMethod);
		
		String indexPath = request.getServletContext().getRealPath("/index");
		//System.out.println(indexPath);
		List<news_info>results = search(indexPath, queryStr);
		if("byHotIndex".equals(sortMethod)) {
			Collections.sort(results, new SortByHotIndex());
		}
		if("byTime".equals(sortMethod)) {
			//System.out.println("安时间排序");
			Collections.sort(results,new SortByTime());
		}
	
		Gson gson=new Gson();
		String info=gson.toJson(results);
		
		//System.out.println("贺坤"+info);
		out.print(info);
        out.flush();
        out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public static List<news_info> search(String indexPath,String queryStr) {  
		List<news_info> result = new ArrayList<news_info>();
		Analyzer analyzer = new StandardAnalyzer();
		System.out.println(queryStr);
        //创建或打开索引目录  
		Directory directory = null;
		try {
			File indexpath = new File(indexPath);
			if (indexpath.exists() != true) {
				indexpath.mkdirs();
			}
			// 设置要查询的索引目录
			Path dir = Paths.get(indexPath);
			directory = FSDirectory.open(dir);
			// 创建indexSearcher
			DirectoryReader dReader = DirectoryReader.open(directory);
			IndexSearcher searcher = new IndexSearcher(dReader); 
			Query query = null;
			TopDocs hits = null;
			
	        String[] fields = {"date", "title" };  
	        BooleanClause.Occur[] clauses = { BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD  };  
	        query = MultiFieldQueryParser.parse(queryStr, fields, clauses, analyzer); 
	        hits = searcher.search(query, 1000);

            ScoreDoc[] scoreDocs = hits.scoreDocs;
            
	        //System.out.println("匹配 '"+queryStr+"'，总共查询到"+ hits.totalHits +"个文档");
	      
	        int begin = 0;
	        int end = 6;
	        for(int i = begin; i < end; i ++){
	        
	           int docID = scoreDocs[i].doc;
	           Document doc = searcher.doc(docID);
	           news_info news = new news_info();
	           news.setTitle(doc.get("title"));news.setUrl(doc.get("url"));news.setCate(doc.get("cate"));
	           news.setDate(doc.get("date"));news.setComment(doc.get("comment"));news.setSrcFrom(doc.get("srcFrom"));
	           news.setHotIndex(doc.get("hotIndex"));news.setContent(doc.get("content"));
	           //news.printNews();
	           result.add(news);
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 

}
