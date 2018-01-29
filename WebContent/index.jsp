<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="cn.java.ir.db.*" %>
    
<%
    List<news_info> arrlist = (List<news_info>)request.getAttribute("hotNews");
%>    
    
<!doctype html>
<html>
    <head>
        <title>欢迎使用小搜搜索</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <script type="text/javascript" src="./javascript/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="./javascript/keyword.js"></script>
        <link href="./css/search.css" rel="stylesheet" type="text/css">
        <style type="text/css">
         *{margin:0 auto}
        </style>
        
        <script type="text/javascript">    
            function a(){  
            	var myDate = new Date();
            	var year = myDate.getFullYear();
            	var mon = myDate.getMonth()+1;
            	var day = myDate.getDay();
            	day = day - 1;
                $.ajax({    
                    url:"serchHotNews?queryStr="+ year+"-"+ mon+"-"+day +"&sortnews=byTime",//servlet文件的名称  
                    type:"GET", 
                    success: function(data){ 
                    	var obj = eval('(' + data + ')');
						var len = obj.length;
						var addStr = '<h3><a href="'+  obj[0].url +'" target="_blank">'+ obj[0].title +'</a></h3><br />'
                    	for(var i=1;i<len;i++)
                        {
                            addStr += '<h3><a href="'+  obj[i].url +'"target="_blank">'+ obj[i].title +'</a></h3><br />'
                        }
                    	document.getElementById("hotNews").innerHTML = addStr;
                    }
                });  
                  
            }  
        </script>    
        
    </head>
    <body onload="a()" style="background:url(images/test2.jpeg);background-attachment: fixed;background-repeat: no-repeat;background-size: cover;">
    	<section class="head-content" style="height:250px">
		</section>
    
        <!--start search-->
        <section class="main-content" >
			<div class="logo" style="text-align: center;">
				<img alt="logo" src="images/ucas_welcome.jpeg" width=200px height=50px>
			</div>

			<div id="search_bg" style="margin-top:20px;margin-bottom:20px;">
			<div id="button_bg">
			    <form action="search" method="post">
			        <input type="text" value="" style="color:#FFFFFF;" x-webkit-speech="" lang="zh-CN" placeholder="点击搜索" name="queryStr" class="textb" autocomplete="off"><!--autocomplete 屏蔽输入自动记录-->
			            <input type="submit" name="sub" value="小搜一下" class="subb">
			        </form>
			        <div class="keyword"></div>
			    </div>
			</div>
		</section>	
        <!--end start-->
        <div>
			<br></br>
			<br></br>
			<br></br>
			<br></br>
			<br></br>
			<h2>实时热点</h2>
					 <!-- 数据开始 -->
			<div id="hotNews"></div>

		 </div>
      
     
        
    </body>
</html>