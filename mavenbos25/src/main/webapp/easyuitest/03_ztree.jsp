<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ztree树</title>
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

<!-- 引入ztree -->
<!-- zTree的js -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ztree/jquery.ztree.all-3.5.js"></script>
<!-- zTree样式 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/ztree/zTreeStyle.css"/>

<script type="text/javascript">
	$(function(){
		//-------标准树
		//1.构建配置对象
		var setting = {	};//默认就是标准树
		//2.构建树的节点(json数组，每一个json是一个节点)
		/* var zNodes=[
		        {name:"百度"},    
		        {name:"搜狐"},    
		        {name:"新浪"},    
		        {name:"腾讯"}  
		            ]; */
		var zNodes=[
		        {name:"搜索引擎",iconOpen:"${pageContext.request.contextPath}/js/ztree/img/diy/1_open.png", iconClose:"${pageContext.request.contextPath}/js/ztree/img/diy/1_close.png",
		        	children:[
		                               //每一个json是一个子节点
		                               {name:"百度",icon:"${pageContext.request.contextPath}/js/ztree/img/diy/3.png"},
		                               {name:"谷歌",icon:"${pageContext.request.contextPath}/js/ztree/img/diy/5.png"}
		                               ]},
		        {name:"金融公司",isParent:true},//父节点
		        {name:"搜狐",url:"http://www.baidu.com"},  //链接  
		        {name:"新浪"},    
		        {name:"腾讯"}  
		            ];
		
		//初始化ztree
		//第一个参数是ul对象，第二个，参数设置对象，第三个节点对象
		$.fn.zTree.init($("#standardZtree"), setting, zNodes);
		
		//-----简单json树（推荐）
		//1.配置对象
		var setting2 = {
				data: {
					simpleData: {
						enable: true
						//idKey:"id"//可以更改节点的id属性的名字，但一般没人改
						
					}
				},
				callback:{
					//点击事件
					//event：事件，treeid：节点编号，treeNode：节点json对象，clickFlag：点击后的类型标识
					onClick:function(event, treeId, treeNode, clickFlag){
						//点击的哪个节点的名字
						//alert(treeNode.name);
						//判断哪个节点有链接，我在弹
						//判断page属性值不为空(js)
						if(treeNode.page!=undefined&&treeNode.page!=""){
							//alert(treeNode.name);
							//判断选项卡存在不存在，如果存在，切换，不新添加
							if($("#mytabs").tabs("exists",treeNode.name)){
								//切换，选中
								$("#mytabs").tabs("select",treeNode.name)
							}else{
								//不存在，添加一个选项卡
								$("#mytabs").tabs("add",{
									title:treeNode.name, //设置菜单的名字为tab的名字
									//content:'我的内容',
									//content:"<iframe frameborder='0' width='100%' height='100%' scrolling='auto' src='"+treeNode.page+"'></iframe>",
									// 开启一个新的tab页面
									content:'<div style="width:100%;height:100%;overflow:hidden;">'
											+ '<iframe src="'
											+ treeNode.page
											+ '" scrolling="auto" style="width:100%;height:100%;border:0;" ></iframe></div>',
									closable:true
									//url,不支持跨域
								});
							}
							
							
							
							
						}
						
						
					}
					
				}
			};
		//2.构建节点对象（简单结构）
		var zNodes2 =[
		          //id：当前节点的编号，pid：父节点的编号
		          {id:"001",pId:"0",name:"友情链接"},//节点,0代表没有父节点
		          //自定义一个连接地址
		          {id:"101",pId:"001",name:"百度",page:"http://www.baidu.com"},
		          {id:"102",pId:"001",name:"谷歌"},
		          {id:"002",pId:"0",name:"谷歌"} 
		             ];
		
		//初始化ztree
		//第一个参数是ul对象，第二个，参数设置对象，第三个节点对象
		$.fn.zTree.init($("#simpleZtree"), setting2, zNodes2);
	});

</script>
</head>
<body class="easyui-layout">
	<!-- <div class="easyui-layout" style="width:600px;height:400px;"> -->
	<div data-options="region:'north' ,border:false" title="北标题" style="height:100px">北</div>
	<div data-options="region:'west',title:'菜单'" style="width:200px">
		<!-- 折叠组件 -->
		<div class="easyui-accordion" data-options="fit:true">
			<div title="菜单1">
				<!-- ztree树-标准json数据 -->
				<ul id="standardZtree" class="ztree"></ul>
			
			</div>
			<div data-options="title:'菜单2',selected:true">
				<!-- ztree树-简单json数据 -->
				<ul id="simpleZtree" class="ztree"></ul>
			
			</div>
			<div data-options="title:'菜单3'">菜单2内容</div>
		</div>
	
	</div>
	<div  data-options="region:'center'" style="padding:5px">
		<!-- tab选项卡组件 -->
		<div id="mytabs" class="easyui-tabs" data-options="border:false,fit:true">
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