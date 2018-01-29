package cn.java.ir.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
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
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.vectorhighlight.BaseFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.search.vectorhighlight.FragListBuilder;
import org.apache.lucene.search.vectorhighlight.FragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.ScoreOrderFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.SimpleFragListBuilder;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import cn.java.ir.db.Page;
import cn.java.ir.db.news_info;
import cn.java.ir.lucence.CreateIndex;


/**
 * Servlet implementation class Searcher
 */
//@WebServlet("/Searcher")
public class Searcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int perpagecount = 5;
	private static double singleSearchNum = 0;
	

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long starTime = System.currentTimeMillis();
		request.setCharacterEncoding("UTF-8"); 
		String queryStr = request.getParameter("queryStr");
		//System.out.println("queryStr = " + queryStr);
		int curPageIndex = 1; 
		String indexStr = request.getParameter("curPageIndex");
		//System.out.println("curIndex = " + indexStr);
		if(request.getParameter("curPageIndex") != null){
			curPageIndex = Integer.parseInt(indexStr);
		}
		//System.out.println("curIndex = " + curPageIndex);
		String indexPath = request.getServletContext().getRealPath("/index");
		//System.out.println(indexPath);
		List<news_info> results = null;
		String sortMethod = (String) request.getParameter("sortnews");
		if(sortMethod != null) {
			results = search(indexPath, queryStr, perpagecount, curPageIndex, sortMethod);
			//System.out.println("sortMethod" + sortMethod);
			if("byTime".equals(sortMethod)) {
				//System.out.println("安时间排序");
				Collections.sort(results,new SortByTime());
			}
			if("byHotIndex".equals(sortMethod)) {
				//System.out.println("安热度排序");
				Collections.sort(results, new SortByHotIndex());
			}
		}else {
			results = search(indexPath, queryStr, perpagecount, curPageIndex, null);
		}
		
		
		Page pages = new Page(curPageIndex, results.size() / perpagecount + 1, perpagecount, results.size(),
				perpagecount * (curPageIndex - 1), perpagecount * curPageIndex, true,curPageIndex==1?false:true);
		long endTime = System.currentTimeMillis();
		double totleTime = (double )(endTime - starTime)/1000;
		
		//System.out.println(totleTime + "\t" + singleSearchNum);
		request.setAttribute("singleSearchNum", singleSearchNum);
		request.setAttribute("searchTime", totleTime);
		request.setAttribute("page", pages);
		request.setAttribute("queryStr", queryStr);
		request.setAttribute("results", results);
		request.getRequestDispatcher("reacherResult.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public static List<news_info> search(String indexPath,String queryStr, int pageSize, int curPageIndex, String sortMethod) {  
		boolean isWildcardSearch = WildCardMatch.isWildCard(queryStr);
		List<news_info> result = new ArrayList<news_info>();
		Analyzer analyzer = new StandardAnalyzer();
		
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
			if(isWildcardSearch) {
				//System.out.println("通配符查询");
				Term t1=new Term("date",queryStr);
				WildcardQuery query1=new WildcardQuery(t1);
				Term t2=new Term("title",queryStr);
				WildcardQuery query2=new WildcardQuery(t1);
				Term t3=new Term("content",queryStr);
				WildcardQuery query3=new WildcardQuery(t2);
				Term t4=new Term("comment",queryStr);
				WildcardQuery query4=new WildcardQuery(t3);
				
				BooleanClause bc1 = new BooleanClause(query1, Occur.SHOULD);
				BooleanClause bc2 = new BooleanClause(query2, Occur.SHOULD);
				BooleanClause bc3 = new BooleanClause(query3, Occur.SHOULD);
				BooleanClause bc4 = new BooleanClause(query4, Occur.SHOULD);
				query = new BooleanQuery.Builder().add(bc1).add(bc2).add(bc3).add(bc4).build();
				
//				Sort sort = new Sort();
//				SortField sf1 = new SortField("date",  SortField.Type.STRING, true);
//				sort.setSort(sf1);
				hits = searcher.search(query, 10);
			}else {
				//System.out.println("普通查询");
				
		        String[] fields = { "date", "title", "content", "comment" };  
		        BooleanClause.Occur[] clauses = { BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD  };  
		        query = MultiFieldQueryParser.parse(queryStr, fields, clauses, analyzer); 
//		        Sort sort = new Sort();
//				SortField sf1 = new SortField("date", SortField.Type.STRING, true);
//				sort.setSort(sf1);
		        hits = searcher.search(query, 1000);
			}
	        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
	        		"<span style='color:red'>", "</span>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
            highlighter.setTextFragmenter(new SimpleFragmenter(200)); 
            ScoreDoc[] scoreDocs = hits.scoreDocs;
            singleSearchNum = hits.totalHits;
            
	        //System.out.println("匹配 '"+queryStr+"'，总共查询到"+ hits.totalHits +"个文档");
	      
	        if(curPageIndex < 1) {curPageIndex = 1;}
	        int begin = pageSize * (curPageIndex - 1);
	        int end = (int) Math.min(begin + pageSize , hits.totalHits);
	        String title = null;
	        String content = null;
	        String comment = null;
	        for(int i = begin; i < end; i ++){
	        
	           int docID = scoreDocs[i].doc;
	           Document doc = searcher.doc(docID);
	           title = highlighter.getBestFragment(analyzer, "title", doc.get("title"));
	           if(title == null) {
	        	   title = doc.get("title");
	           }
	           //System.out.println(title);
	           content = highlighter.getBestFragment(analyzer, "content", doc.get("content"));
	           if(content == null) {
	        	   content = doc.get("content");
	           }
	           comment = highlighter.getBestFragment(analyzer, "comment", doc.get("comment"));
	           if(comment == null) {
	        	   comment = doc.get("comment");
	           }
	           
	           news_info news = new news_info();
	           news.setTitle(title);news.setUrl(doc.get("url"));news.setCate(doc.get("cate"));
	           news.setDate(doc.get("date"));news.setComment(comment);news.setSrcFrom(doc.get("srcFrom"));
	           news.setHotIndex(doc.get("hotIndex"));news.setContent(content);
	           result.add(news);
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	 
	
	 
}
