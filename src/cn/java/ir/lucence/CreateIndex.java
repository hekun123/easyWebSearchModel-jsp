package cn.java.ir.lucence;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import cn.java.ir.db.*;

public class CreateIndex {

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
		Connection con = DBStatement.getDbConnection();
		String sql = "select * from news_info;";
		ResultSet records = DBStatement.getResultSet(con, sql);
		
		getIndex(records);
		
	}
	
	public static void getIndex(ResultSet records) throws SQLException, IOException {
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		Directory directory = null;
		IndexWriter indexWriter = null;
		try {
			File indexpath = new File("./WebContent/index");
			if (indexpath.exists() != true) {
				indexpath.mkdirs();
			}
			Path dir = Paths.get("./WebContent/index");
			directory = FSDirectory.open(dir);
			indexWriter = new IndexWriter(directory, indexWriterConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 0;
		while(records.next()) {
			System.out.println(records.getString("title")+records.getString("url")+records.getString("cate")
			+records.getString("date")+records.getString("srcFrom")+records.getString("content")+records.getString("comment")+records.getString("hotIndex"));
			Document doc=new Document();
			doc.add(new TextField("title", records.getString("title"), Field.Store.YES)); 
			doc.add(new TextField("url", records.getString("url"), Field.Store.YES));
			doc.add(new TextField("cate", records.getString("cate"), Field.Store.YES));
			doc.add(new TextField("date", records.getString("date"), Field.Store.YES));
			doc.add(new TextField("srcFrom", records.getString("srcFrom"), Field.Store.YES));  
			doc.add(new TextField("content", records.getString("content"), Field.Store.YES)); 
			doc.add(new TextField("comment", records.getString("comment"), Field.Store.YES));
			doc.add(new TextField("hotIndex", records.getString("hotIndex"), Field.Store.YES));
//			doc.add(new TextField("editor", records.getString("editor"), Field.Store.YES));  
			indexWriter.addDocument(doc);  
		}
		indexWriter.commit();
		System.out.println("numDocs"+indexWriter.numDocs()); 
		indexWriter.close(); 
		directory.close(); 
	}
	

}
