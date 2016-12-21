package cn.itcast.bos.web.action.auth;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.service.auth.RoleService;
import cn.itcast.bos.web.base.BaseAction;

//角色的action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role>{
	//注入service
	@Autowired
	private RoleService roleService;
	
	//列表查询
	@Action("role_list")
	public String list(){
		List<Role> roleList= roleService.findRoleList();
		ActionContext.getContext().getValueStack().push(roleList);
		return JSON;
	}
	//保存角色（关联权限）
	@Action(value="role_save" ,results={@Result(name=SUCCESS,location="/WEB-INF/pages/admin/role.jsp")})
	public String save(){
		//调用业务层
		roleService.saveRole(model,functionIds);
		
		return SUCCESS;
	}
	
	//属性驱动
	private String functionIds;
	public void setFunctionIds(String functionIds) {
		this.functionIds = functionIds;
	}
	

}
