package cn.itcast.bos.web.action.qp;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.qp.WorkOrderManage;
import cn.itcast.bos.service.qp.WorkOrderManageService;
import cn.itcast.bos.web.base.BaseAction;

//工作单管理-
@ParentPackage("basic-bos")
@Namespace("/")
@Controller
@Scope("prototype")
public class WorkOrderManageAction extends BaseAction<WorkOrderManage>{
	//注入service
	@Autowired
	private WorkOrderManageService workOrderManageService;

	//快速录入保存
	@Action("workOrderManage_save")
	public String save(){
		//结果集
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//调用业务层
			workOrderManageService.saveWorkOrderManage(model);
			resultMap.put("result", true);
		} catch (Exception e) {
			resultMap.put("result", false);
		}
		//压入栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		return JSON;
	}
	
	//分页条件列表查询
	@Action("workOrderManage_listPage")
	public String listPage(){
		//分页条件
		Pageable pageable = new PageRequest(page-1, rows);
		
		//分页结果对象
		Page<WorkOrderManage> page;
		
		if(StringUtils.isNotBlank(conditionName)&&StringUtils.isNotBlank(conditionValue)){
			//如果有业务条件-先将条件调用lucence查询，然后得到的id，再从数据库查询---被Hibernate search
			page=workOrderManageService.findWorkOrderManageList(pageable,conditionName,conditionValue);
			
		}else{
			//没有业务条件-纯分页
			page= workOrderManageService.findWorkOrderManageList(pageable);
		}
		//最终要将page分解
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", page.getContent());
		//压入栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
	}
	
	//属性驱动
	private String conditionName;
	private String conditionValue;

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}
	
	
}
