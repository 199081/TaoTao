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

</head>
<body >
	<!-- 菜单按钮 -->
	<a href="javascript:void(0)" class="easyui-menubutton" data-options="menu:'#mm',iconCls:'icon-edit'">用户菜单</a>
	<a >控制面板</a>
	<!-- 菜单 -->
	<div id="mm" style="width:150px;"> 
		<!-- 每一个div就是一行菜单 -->
		<div>添加</div>
		<div>修改</div>
		<!-- 分割线 -->
		<div class="menu-sep"></div> 
		<div>删除</div>
	</div>

</body>
</html>