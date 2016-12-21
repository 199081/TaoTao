package cn.itcast.bos.web.action.auth;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.auth.FunctionService;
import cn.itcast.bos.web.base.BaseAction;

//功能权限的action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller
@Scope("prototype")
public class FunctionAction extends BaseAction<Function>{
	//注入service
	@Autowired
	private FunctionService functionService;

	//列表查询
	@Action("function_list")
	public String list(){
		//无需分页
		List<Function> functionList= functionService.findFunctionList();
		//直接压栈顶
		ActionContext.getContext().getValueStack().push(functionList);
		return JSON;
	}
	
	@Action(value="function_save",results={@Result(name=SUCCESS,location="/WEB-INF/pages/admin/function.jsp")})
	public String save(){
		functionService.saveFunction(model);
		return SUCCESS;
	}
	
	//显示当前用户的所拥有的菜单
	@Action("function_showmenu")
	public String showmenu(){
		//获取当前登录用户(用户放入shiro的session中了)
		//这个session和httpsession是否是一个session
		Subject subject = SecurityUtils.getSubject();
		//shiro可以用在任何的框架和环境下（java）
		//java应用（不是web应用）也是将用户放入session--》shiro自己的session
		//一旦web应用，shiro的session会包装httpsession。此时可以认为两者等同
//		subject.getSession();
		User user=(User)subject.getPrincipal();//当前登录用户
		
		//调用业务层，查询菜单
//		functionService.findMenu(当前登录用户);
		List<Function> functionList= functionService.findFunctionByUser(user);
		//压栈顶
		ActionContext.getContext().getValueStack().push(functionList);
		return JSON;
	}
}
