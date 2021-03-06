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

<script type="text/javascript">

	//初始化表格
	$(function(){
		$("#mytable").datagrid({
			//数据
			url:'data2.json',
			//头部信息（麻烦-二维数据）
			columns:[[ 
			{field:'id',title:'编号',checkbox:true}, 
			//editor:type
			{field:'name',title:'名称',width:200,editor:{
				type:'validatebox',//编辑的组件的类型，可以文本框（validatebox），或者日期组件，easyui的组件
				options:{required:true}//编辑的组件的参数,相当于data-options
			}}, 
			{field:'price',title:'价格',width:100,align:'right'} 
			]],
			rownumbers:true,
			fitColumns:true,
			//编辑完成的事件
			//参数1：编辑行的索引，编辑到第几行了，从0开始
			//参数2：当前行的json数据（包含编辑和未编辑的数据）
			//参数3：当前行被编辑的数据
			onAfterEdit:function(rowIndex, rowData, changes){
				//alert(rowData.name+"----"+rowData.price)
				alert(changes.name+"----"+changes.price)
			},
			pagination:true,
			//工具条传一个数组，数组中每一个json对象就是一个按钮
			toolbar: [{  		
				  iconCls: 'icon-add', //图标
				  text:"添加",//不要文字可以不加
				  //点击事件
				  handler: function(){alert('保存调用了。。。。')}  	
				  },'-',//分割线
				  {  		
				  iconCls: 'icon-help',  		
				  handler: function(){alert('帮助调用了。')}  	
				  },{  		
					  iconCls: 'icon-edit', 
					  text:"编辑选中的行",
					  handler: function(){
						  //获取选中的行
						  var row = $("#mytable").datagrid("getSelected");
						  //获取行的索引
						  var rowIndex = $("#mytable").datagrid("getRowIndex",row);
						  //打开当前行的编辑功能
						  $("#mytable").datagrid("beginEdit",rowIndex);
					  	}  	
					  }
				  ,{  		
					  iconCls: 'icon-save', 
					  text:"保存编辑的数据",
					  handler: function(){
						  //获取选中的行
						  var row = $("#mytable").datagrid("getSelected");
						  //获取行的索引
						  var rowIndex = $("#mytable").datagrid("getRowIndex",row);
						  //打开当前行的编辑功能
						  $("#mytable").datagrid("endEdit",rowIndex);
					  	}  	
					  }
				  ,{  		
					  iconCls: 'icon-cancel', 
					  text:"取消编辑的数据",
					  handler: function(){
						  //获取选中的行
						  var row = $("#mytable").datagrid("getSelected");
						  //获取行的索引
						  var rowIndex = $("#mytable").datagrid("getRowIndex",row);
						  //打开当前行的编辑功能
						  $("#mytable").datagrid("cancelEdit",rowIndex);
					  	}  	
					  }
				  ,{  		
					  iconCls: 'icon-add', 
					  text:"添加插入一行",
					  handler: function(){
						  //打开当前行的编辑功能
						  $("#mytable").datagrid("insertRow",{
							  index:0,//开头插入
							  row:{
								  'id':'1001',
								  'name':'电脑',
								  'price':'180'
								  
							  }
						  });
					  	}  	
					  }
				  ]  

			
			
		});
		
	});
</script>

</head>
<body >
	<h1>4.9.1.	方式一：DataGrid加载HTML静态数据（了解）</h1>
	<table class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'id'">编号</th>
				<th data-options="field:'name',width:200">名称</th>
				<th data-options="field:'price'">价格</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>1</td>
				<td>电视机</td>
				<td>998</td>
			</tr>
			<tr>
				<td>2</td>
				<td>电冰箱</td>
				<td>9999</td>
			</tr>
		</tbody>
	</table>
	<h1>4.9.2.	方式二：加载Json远程数据</h1>
	<table class="easyui-datagrid" data-options="url:'data2.json',pagination:true,toolbar:'#mytoolbtn',rownumbers:true,fitColumns:true">
		<thead>
			<tr>
				<th data-options="field:'id',checkbox:true">编号</th>
				<th data-options="field:'name',width:200">名称</th>
				<th data-options="field:'price1',width:100"">价格</th>
			</tr>
		</thead>
	</table>
	<!-- 按钮 -->
	<div id="mytoolbtn">
		<!-- 按钮就是linkbutton -->
		<a id="add" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-help'"/>
		<a href="javascript:void(0)" class="easyui-linkbutton" >添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">更新</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-cancel'">删除</a>
	</div>
	<h1>4.9.3.	方式三：使用JavaScript来加载数据(表格也用js初始化)</h1>
	<table id="mytable" class="easyui-datagrid" ></table>

</body>
</html>