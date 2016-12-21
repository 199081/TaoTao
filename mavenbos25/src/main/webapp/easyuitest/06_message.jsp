<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<!-- 引入jQuery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
<!-- EasyUI核心 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<!-- EasyUI国际化 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<!-- EasyUI主题样式CSS -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css"/>
<!-- EasyUI图标 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css"/>
<script type="text/javascript">
	$(function(){
		//程序运行到这里就调用js
		//1.第一种：警告框
		//$.messager.alert("我是标题","我是内容","error");

		//2.确认
		/* $.messager.confirm("询问","您吃饭了么",function(r){
			//r:true（点击确定）或false（点击取消）
			if(r){
				alert("已经吃饭了");
			}else{
				alert("饿着呢。。。");
			}
		}); */
		//输入
		/* $.messager.prompt("个人信息","您的姓名是什么?",function(msg){
			alert(msg);
		}); */
		//底部弹出框
		/* $.messager.show({
			title:"商品推荐",
			msg:"1元抢购了。。。",
			timeout:3000,
			showType:"fade"
			
		}); */
		//进度条
		$.messager.progress({
			interval:1000
		});
		
		window.setTimeout("$.messager.progress('close')",3000);
	});
	
</script>
</head>
<body >
	

</body>
</html>