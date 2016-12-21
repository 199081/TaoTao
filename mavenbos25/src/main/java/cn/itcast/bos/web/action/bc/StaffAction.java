package cn.itcast.bos.web.action.bc;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.service.bc.StaffService;
import cn.itcast.bos.web.base.BaseAction;

//取派员管理的action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller
@Scope("prototype")
public class StaffAction extends BaseAction<Staff> {
	//注入业务层
	@Autowired
	private StaffService staffService;

	//保存取派员
	@Action(value="staff_save",results={@Result(name=SUCCESS,location="/WEB-INF/pages/base/staff.jsp")})
	public String save(){
		//调用业务层
		staffService.saveStaff(model);
		
		return SUCCESS;
	}
	
	//列表分页查询
	@Action(value="staff_listpage")
	public String listpage(){
		//调用业务层，查询分页数据
		//将分页参数（当前页码+每页最大记录数）封装到pageable对象中
		//这两个参数，你可以最原始的servlet，struts值栈都可以
		//spring data jpa：第一页值0，但datagrid第一页值是1
		Pageable pageable =new PageRequest(page-1, rows);
		//调用业务层查询分页数据
		Page<Staff> page= staffService.findStaffListPage(pageable);
		
		//数据压入栈顶，交给struts2的json插件(自动调用对象的getter方法)
//		ActionContext.getContext().getValueStack().push(page);
		//重新组装数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", page.getTotalElements());//总记录数
		resultMap.put("rows", page.getContent());//业务数据列表
		ActionContext.getContext().getValueStack().push(resultMap);
		
		//返回json类型的结果集
		return JSON;
		
	}
	
	//属性驱动:自动封装参数到属性中
	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	//批量删除（作废）派送员
	@Action(value="staff_deletebatch")
	public String deletebatch(){
		//获取参数：
		String ids = super.getValueFromParameter("ids");
		//分割字符串的业务可以在这里搞，也可以在业务层
		
		//结果map
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			//调用业务层
			staffService.deleteStaffBatch(ids);
			resultMap.put("result", true);
		} catch (Exception e) {
			resultMap.put("result", false);
			e.printStackTrace();
		}
		//压入栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
		//		return SUCCESS;//返回列表，可以跳转到该结果集的页面上即可。
		
	}
	
	@Action("staff_listNoDeltag")
	public String listNoDeltag(){
		//查询数据
		List<Staff> staffList= staffService.findStaffListNoDeltag();
		//压入栈顶
		ActionContext.getContext().getValueStack().push(staffList);
		
		return JSON;
	}
	

	
	
}
