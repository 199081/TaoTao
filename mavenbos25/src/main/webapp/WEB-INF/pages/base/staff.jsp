<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 引入shiro的标签 -->
<%@taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<!-- 导入jquery核心类库 -->
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-1.8.3.js"></script>
<!-- 导入easyui类库 -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/js/easyui/ext/portal.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/default.css">	
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.portal.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/easyui/ext/jquery.cookie.js"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<script
	src="${pageContext.request.contextPath }/js/easyui/outOfBounds.js"
	type="text/javascript"></script>
<script type="text/javascript">
	function doAdd(){
		//alert("增加...");
		$('#addStaffWindow').window("open");
	}
	
	function doView(){
		alert("查看...");
	}
	
	//作废派送员
	function doDelete(){
		//alert("删除...");
		//先获取选中的行
		var rows = $("#grid").datagrid("getSelections");
		
		//先判断是否选中了，选中了才能作废操作
		if(rows.length==0){
			//提示：
			$.messager.alert("警告","必须至少选择一个派送员","warning");
			return;
		}
		//作废操作:异步请求操作，传入参数，要作废的人员的id
		
		//姜数组对象转换为json对象（字符串的逗号分割-多选框name一样-String[]）
		//弄一个数组
		var rowsArray= new Array();
		
		for(var i=0;i<rows.length;i++){
			rowsArray.push(rows[i].id);
		}
		//转字符串
		var ids=rowsArray.join(",");
		
		//参数：两种name=rose&age=18,{}[]
		$.post("${pageContext.request.contextPath}/staff_deletebatch.action",{"ids":ids},function(data){
			//data:结果{result:true}
			if(data.result){
				//删除成功
				$.messager.alert("信息","作废成功","info");
				//刷新grid
				$("#grid").datagrid("reload");
				$("#grid").datagrid("unselectAll");
			}else{
				//作废失败
				$.messager.alert("失败","作废失败","error");
			}
			
		});
		
	}
	
	function doRestore(){
		alert("将取派员还原...");
	}
	//工具栏
	var toolbar = [ {
		id : 'button-view',	
		text : '查询',
		iconCls : 'icon-search',
		handler : doView
	},
	<shiro:hasPermission name="staff:add">
	 {
		id : 'button-add',
		text : '增加',
		iconCls : 'icon-add',
		handler : doAdd
	},
	</shiro:hasPermission>
	 {
		id : 'button-delete',
		text : '作废',
		iconCls : 'icon-cancel',
		handler : doDelete
	},{
		id : 'button-save',
		text : '还原',
		iconCls : 'icon-save',
		handler : doRestore
	}];
	// 定义列
	var columns = [ [ {
		field : 'id',
		checkbox : true,
	},{
		field : 'name',
		title : '姓名',
		width : 120,
		align : 'center'
	}, {
		field : 'telephone',
		title : '手机号',
		width : 120,
		align : 'center'
	}, {
		field : 'haspda',
		title : '是否有PDA',
		width : 120,
		align : 'center',
		//格式化
		//formatter : function(data,row, index){
			//参数value：字段的值，rowdata：某一行的数据，rowIndex：当前行的索引。
		formatter : function(value,rowData, rowIndex){
			if(value=="1"){
				return "有";
			}else{
				return "无";
			}
		}
	}, {
		field : 'deltag',
		title : '是否作废',
		width : 120,
		align : 'center',
		formatter : function(data,row, index){
			if(data=="0"){
				return "正常使用"
			}else{
				return "已作废";
			}
		}
	}, {
		field : 'standard',
		title : '取派标准',
		width : 120,
		align : 'center'
	}, {
		field : 'station',
		title : '所谓单位',
		width : 200,
		align : 'center'
	} ] ];
	
	$(function(){
		// 先将body隐藏，再显示，不会出现页面刷新效果
		$("body").css({visibility:"visible"});
		
		// 取派员信息表格
		$('#grid').datagrid( {
			iconCls : 'icon-forward',
			fit : true,
			border : false,
			rownumbers : true,
			striped : true,
			//pageList: [30,50,100],
			pageList: [2,4,8],
			pagination : true,
			toolbar : toolbar,
			//url : "json/staff.json",
			url : "${pageContext.request.contextPath}/staff_listpage.action",
			idField : 'id',
			columns : columns,
			onDblClickRow : doDblClickRow
		});
		
		// 添加取派员窗口
		$('#addStaffWindow').window({
	        title: '添加取派员',
	        width: 400,
	        modal: true,
	        shadow: true,
	        closed: true,
	        height: 400,
	        resizable:false
	    });
		
		//给保存按钮绑定事件
		$("#save").click(function(){
			
			//表单校验
			if($("#staffForm").form("validate")){
				//通过，提交表单
				//alert(1);
				$("#staffForm").submit();//jquery
			}
			
		});
		
	});

	//打开修改窗口
	//参数1：点击的行的索引，参数2rowData：当前行的json数据
	function doDblClickRow(rowIndex, rowData){
		//alert("双击表格数据...");
		//alert(rowData.name);
		$("#addStaffWindow").window("open");
		//填表单
		$("#staffForm").form("load",rowData);//不填充隐藏域
		
	}
	
	//自定义的easyui的表单校验规则
	//手机号校验
	$.extend($.fn.validatebox.defaults.rules, { 
		//校验规则的名字，随意
		telephone: { 
			//参数1：表单中输入框的值，参数2：校验器的参数
			validator: function(value, param){ 
				//校验手机号正则表达式
				var regexp=/^1[3|4|5|7|8]\d{9}$/;
				return regexp.test(value);//方法校验
			}, 
			//校验不成功的提示信息
			message: '您输入的手机号码不正确' 
			} 
	}); 
	
	function closeWindow(){
		//清除表单
		$("#staffForm").form("clear");
		//如果你调用dom对象的form表单重置
		//$("#staffForm").reset();//jquery无法直接调用reset
		//转成dom对象
		//$("#staffForm").get(0).reset();
		//$("#staffForm")[0].reset();
		
	}

	
</script>	
</head>
<body class="easyui-layout" style="visibility:hidden;">

	<div region="center" border="false">
		<shiro:hasPermission name="region">aaaaa</shiro:hasPermission>
    	<table id="grid"></table>
	</div>
	<div class="easyui-window" title="对收派员进行添加或者修改" id="addStaffWindow" data-options="onClose:closeWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false" >
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="javascript:void(0)" class="easyui-linkbutton" plain="true" >保存</a>
			</div>
		</div>
		
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="staffForm" action="${pageContext.request.contextPath }/staff_save.action" method="post">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">收派员信息</td>
					</tr>
					<!-- TODO 这里完善收派员添加 table -->
					<!-- <tr>
						<td>取派员编号</td>
						<td><input type="text" name="id" class="easyui-validatebox" required="true"/></td>
					</tr> -->
					<tr>
						<td>姓名</td>
						<td><input type="text" name="name" class="easyui-validatebox" data-options="required:true,validType:'length[2,10]'"/>
						<!-- id隐藏域 -->
						<input type="hidden" name="id"/>
						</td>
					</tr>
					<tr>
						<td>手机</td>
						<td><input type="text" name="telephone" class="easyui-validatebox" required="true" 
						data-options="validType:'telephone'"/></td>
					</tr>
					<tr>
						<td>单位</td>
						<td><input type="text" name="station" class="easyui-validatebox" required="true"/></td>
					</tr>
					<tr>
						<td colspan="2">
						<input type="checkbox" name="haspda" value="1" />
						是否有PDA</td>
					</tr>
					<tr>
						<td>取派标准</td>
						<td>
							<input type="text" name="standard" class="easyui-validatebox" required="true"/>  
						</td>
					</tr>
					</table>
			</form>
		</div>
	</div>
</body>
</html>	