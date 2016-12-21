package cn.itcast.bos.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.UserService;
import cn.itcast.bos.utils.MD5Utils;
import cn.itcast.bos.web.base.BaseAction;

//用户操作的action
@Controller("userAction")
//@ParentPackage("struts-default")
//@ParentPackage("json-default")//无全局结果集的定义
@ParentPackage("basic-bos")//使用自定义包
@Namespace("/")
@Scope("prototype")
//@Results({@Result(name=BaseAction.JSON,type=BaseAction.JSON)})//全局结果集
public class UserAction extends BaseAction<User>{
	//注入业务层
	@Autowired
	private UserService userService;
	
	//登录业务
	@Action(value="user_login",results={@Result(name="login",location="/login.jsp")
	,@Result(name="success",type="redirect", location="/index.jsp")})
	@InputConfig(resultName="login")
	public String login(){
		//验证码判断
		//页面上的用户输入验证码
		//要拿到页面的数据可以通过值栈（属性驱动），也可以直接通过request参数
		String userCheckcode = super.getValueFromParameter("checkcode");
		//session中的验证码
		String sessionCheckcode=super.getValueFromSession("key")==null?null:(String)super.getValueFromSession("key");
		
		//判断：两者是否一致
		if(StringUtils.isBlank(sessionCheckcode)||!userCheckcode.equals(sessionCheckcode)){
			//给用户提示错误信息(字段错误)
			super.addFieldError("checkcode", this.getText("UserAction.checkcodeerror"));
			//跳回登录
			return LOGIN;
		}
		
		
		//传统的手动认证：先从业务层查询用户（根据用户名和密码）
//		User loginUser = userService.login(model.getUsername(), model.getPassword());
//		//判断
//		if(null ==loginUser){
//			//登录失败
//			//显示错误信息到页面
//			super.addActionError(this.getText("UserAction.loginfail"));
//			//跳转到登录页面
//			return LOGIN;
//		}else{
//			//登录成功
//			//将用户放入session
//			super.setObjectToSession("user", loginUser);
//			//跳转到首页
//			return SUCCESS;
//		}
		//shiro认证
		
		//获取认证对象（用户）包装对象
		Subject subject = SecurityUtils.getSubject();
		
		//获取一个认证的令牌:
		//直接获取页面的用户名和密码进行校验
		AuthenticationToken authenticationToken = new UsernamePasswordToken(model.getUsername(), MD5Utils.md5( model.getPassword()));
		//认证过程...(“自己认证”)
		try {
			// 如果成功，就不抛出异常，会自动将用户放入session的一个属性。
			subject.login(authenticationToken);
			// 成功，首页
			return SUCCESS;
		} catch (UnknownAccountException e) {
			//e.printStackTrace();
			//提示用户名不存在
			super.addActionError(this.getText("UserAction.usernamenoexsit"));
			// 登录页面
			return LOGIN;
		} catch (IncorrectCredentialsException e) {
			//e.printStackTrace();
			//提示密码不正确
			super.addActionError(this.getText("UserAction.passworderror"));
			// 登录页面
			return LOGIN;
		} catch (AuthenticationException e) {
			// 认证失败
			e.printStackTrace();
			// 登录页面
			return LOGIN;
		}
	}

	//注销退出
	@Action(value="user_loginout",results={@Result(name="login",type="redirect", location="/login.jsp")})
	public String loginout(){
		//传统的退出：销毁session的对象
//		ServletActionContext.getRequest().getSession().invalidate();
		//shiro退出：调用自己的退出
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		//跳转到登录页面
		return LOGIN;
	}
	
	//修改密码
	//@Action(value="user_editpassword",results={@Result(name="success",type="json")})
//	@Action(value="user_editpassword",results={@Result(name=SUCCESS,type=JSON)})
//	@Action(value="user_editpassword",results={@Result(name=JSON,type=JSON)})
	@Action(value="user_editpassword")
	public String editpassword(){
		//获取session的当前登录用户的id，用于修改密码的条件(无需判断用户为空)
		User loginUser=(User) ServletActionContext.getRequest().getSession().getAttribute("user");
		
		//弄一个map
		Map<String, Object> resultMap=new HashMap<String, Object>();
		
		try {
			//获取到密码的值,传递给业务层，进行修改
			userService.updateUserForPassword(loginUser.getId(), model.getPassword());
			//成功，返回成功
			resultMap.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			//如果发生了异常，就返回失败
			resultMap.put("result", false);
		}
		
		//将压入栈顶（json插件：栈顶，root属性-ognl表达式）
		ActionContext.getContext().getValueStack().push(resultMap);
		
//		return SUCCESS;
		return JSON;
	}
	
	//保存用户（关联角色）
	@Action(value="user_save",results={@Result(name=SUCCESS,location="/WEB-INF/pages/admin/userlist.jsp")})
	public String save(){
		//业务层调用
		userService.saveUser(model,roleIds);
		return SUCCESS;
	}
	
	//属性驱动
	private String[] roleIds;
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	
	//用户列表
	@Action("user_list")
	public String list(){
		ActionContext.getContext().getValueStack().push(userService.findAllUser());
		return JSON;
	}
	
}
