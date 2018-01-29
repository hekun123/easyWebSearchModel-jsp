<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="cn.java.ir.db.*" %>
<%
	Page pageInfo = (Page) request.getAttribute("page");
	String queryStr = (String) request.getAttribute("queryStr");
	double searchTime = (double) request.getAttribute("searchTime");
	double singleSearchNum = (double) request.getAttribute("singleSearchNum");
	List<news_info> arrlist = (List<news_info>)request.getAttribute("results");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript" src="./javascript/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="./javascript/keyword.js"></script>
<link href="./css/search.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">  
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
<style type="text/css">
*{margin:0 auto}
#previewSearch 
{
  display:block; 
  position:absolute;
  top:15%;
  left:30%; 
  width:50%; 
  height:50%;    
 }    
 
.div-inline{ float:left;} 
</style>

<script type="text/javascript">
function a(str){
	
	document.getElementById("mydiv").innerHTML = '<iframe id="previewSearch" name="previewSearch"  src="'+ str +'"  ></iframe>'
}
function b(){
	document.getElementById("previewSearch").remove();
}
function c(str){
	$.ajax({
        url:"http://suggestion.baidu.com/su",
        type:"GET",
        dataType:"jsonp",
        jsonp: 'jsoncallback',
        async: false,
        timeout: 5000,//请求超时
        data:{
            "wd":str,
            "cb":"keydata"
        },
        success: function (json) {
        },
        error: function (xhr) {
            return;
        }
    });
}
function keydata(keys){
    var len=keys.s.length;
	var spans = "<span><a href=\"search?queryStr="+keys.s[0]+"&curPageIndex=1\">"+keys.s[0]+"</a></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    for(var i=1;i<len;i++)
    {
        spans+="<span><a href=\"search?queryStr="+keys.s[i]+"&curPageIndex=1\">"+keys.s[i]+"</a></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        if(i%3 == 0){
        	spans+="<br />";
        }
        if(i > 10)break;
    }
    
    document.getElementById("relateSearch").innerHTML = spans;
}
</script>

<title>搜索<%= queryStr %></title>
</head>
<body onload="c('<%= queryStr %>')">

		<div class="nav">
			<div class="nav_left" style="text-align: center;">
				<a href="index.jsp"><img alt="logo" src="images/ucas_search.jpeg"></a>
			</div>
			<div class="nav_form">
				<div id="search_bg" >
					<div id="button_bg">
				    <form action="search" method="post" style="float: left;" >
				        <input type="text" value="<%= queryStr %>" x-webkit-speech="" lang="zh-CN" name="queryStr" class="textb" autocomplete="off"><!--autocomplete 屏蔽输入自动记录-->
			            <input type="submit" name="sub" value="小搜一下" class="subb">
			            <div class="sortPick" style="text-align: center;">
						     <font>按相关度排序<input type="radio" value="byreRelevancy" name="sortnews"  checked="checked" >
							 按时间排序 	<input type="radio"  value="byTime" name="sortnews"  >
							 按热度排序 	<input type="radio"  value="byHotIndex" name="sortnews"  > </font> 
				     	</div>
			        </form>
			    	</div>
				</div>
			</div>
		</div>
		
		
		<br />
		<div id="mydiv" style="height:10px;display: block;"></div>
		<div class="display: block;">
			<div style="width:40%;height:10%;"></div>
			<div style="width:60%;height:10%;">
				<font color="#A2B5CD" style="font-size:13px;">小搜为您共搜索到:<%= singleSearchNum %>,共用时:<%=  searchTime%>s</font>
			</div>
		</div>
		<div class="main-content">
		
			<div class="main-left" style="display: block;width: 10%;float:left;border: 2px solid white;height:60%;"></div>
			<div class="main-main" style="display: block;width: 60%;height:60%;">
			<%
				if (arrlist.size() > 0) {
					Iterator<news_info> iter = arrlist.iterator();
					news_info news;
					while (iter.hasNext()) {
						news = iter.next();
						String searchUrl = news.getUrl();
			%>
			<dl>
				<dt>
						<a onmousemove="a('<%= searchUrl %>')" onmouseout="b()" href="<%=news.getUrl()%> " target="_blank"><%=news.getTitle()%></a>
				</dt>
				<dd>
					<font><%= news.getDate()%></font><br \>
					<%=news.getContent()%>
					
				</dd>
				<dd>
					<a href="<%=news.getUrl()%> "><%=news.getUrl()%></a>&nbsp;&nbsp;&nbsp;
					<a href="search?queryStr=<%= news.getSrcFrom()+news.getCate() %>&curPageIndex=1">查看相关新闻>></a>
				</dd>
			</dl>
			<% 
						}
					}
			%>
		
			</div>
		<div class="main-right" style="display: block;width:100%;height: 60%;"></div>
	</div>
	<div class="foot-content" >
		<div style="display: block;width:40%;height:20%;"></div>
		<div style="display: block;width:60%; height:20%;">
			<h3>相关搜索：</h3>
			<div class="div-inline" id="relateSearch" style="width: 100%;"></div>
	
			<div>
				<ul class="pagination">
					<li><a href="search?queryStr=<%= queryStr %>&curPageIndex=1" >首页</a></li>
					<li><a href="search?queryStr=<%= queryStr %>&curPageIndex=<%=pageInfo.getPage()-1==0?1:pageInfo.getPage()-1%>">上一页</a></li>
					<%
						for (int i = 1; i <= 10; i++) {
					%>
					<li><a href="search?queryStr=<%= queryStr %>&curPageIndex=<%=i%>"><%=i%></a></li>
					<%
						}
					%>
					<li><a href="search?queryStr=<%= queryStr %>&curPageIndex=<%=pageInfo.getPage()+1%>">下一页</a></li>
				</ul>
			</div>
		</div>
	</div>
	
</body>
</html>