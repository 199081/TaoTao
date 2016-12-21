<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>可折叠组件</title>
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
<body class="easyui-layout">
	<!-- <div class="easyui-layout" style="width:600px;height:400px;"> -->
	<div data-options="region:'north' ,border:false" title="北标题" style="height:100px">北</div>
	<div data-options="region:'west',title:'菜单'" style="width:200px">
		<!-- 折叠组件 -->
		<div class="easyui-accordion" data-options="fit:true">
			<div title="菜单1">菜单1内容</div>
			<div data-options="title:'菜单2',selected:true">菜单2内容</div>
			<div data-options="title:'菜单3'">菜单2内容</div>
		</div>
	
	</div>
	<div data-options="region:'center'" style="padding:5px">
		<!-- tab选项卡组件 -->
		<div class="easyui-tabs" data-options="border:false,fit:true">
			<div title="选项卡1">内容1</div>
			<div data-options="title:'选项卡2',selected:true">内容2</div>
			<div data-options="title:'选项卡2',closable:true">内容3</div>
		</div>
	</div>
	<div data-options="region:'east',title:'个人区域'" style="width:100px">东</div>
	<div data-options="region:'south'" style="height:100px">南</div>
<!-- </div> -->
</body>
</html>