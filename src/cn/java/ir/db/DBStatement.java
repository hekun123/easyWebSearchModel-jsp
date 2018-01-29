package cn.java.ir.db;

import cn.java.ir.action.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class DBStatement {
	
	public static void main(String[] args) throws SQLException {
//	      int nums = indexBuilder(new File("Dest_Index_Path"), new File("Test_File_Path"));
//	      System.out.println("doc counts is : " + nums);
		  Connection con = getDbConnection();	
		  if(!con.isClosed()) {
			  System.out.println("success!");
		  }else {
			  System.out.println("falied!");
		  }
		  String sql = "select * from news_info where date = '12-12'";
		  ResultSet records = getResultSet(con, sql);
		  while(records.next()) {
			  System.out.println(records.getString("title"));
		  }  
    }
 
	 public static ResultSet getResultSet(Connection conn, String sql) {
		Statement statmt = null;
        ResultSet rs = null;
        try {
            statmt = conn.createStatement();
            rs = statmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
	 public static Connection getDbConnection() throws SQLException {
		
		Connection conn = null;
		String user = "root";
		String password = "123";
		String url = "jdbc:mysql://localhost:3306/sina_news";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,password); 	
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(!conn.isClosed())
        {
        	return conn;
        }else {
        	return null;
        }
	}
	 
	    
}
