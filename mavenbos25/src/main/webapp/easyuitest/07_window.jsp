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
		$("#tanBtn").click(function(){
			$('#mywindow').window('open'); 
		});
	});
	
</script>
</head>
<body >
	<div id="mywindow" class="easyui-window" style="width:600px;height:400px" data-options="iconCls:'icon-save',modal:true,closed:true"> 
		我是弹出窗口啊
		</br>
		一般是表单
		<input type="text" name="username"/>
		</br>
		</br>
		<input type="submit" value="提交"/>
		<a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">重置</a>
	</div>
	
	<input type="button" id="tanBtn" value="我要弹"/>
</body>
</html>