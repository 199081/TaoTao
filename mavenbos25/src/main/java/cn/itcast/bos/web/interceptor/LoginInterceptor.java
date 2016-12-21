package cn.itcast.bos.web.interceptor;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

//登录拦截器
@Controller("loginInterceptor")
public class LoginInterceptor extends MethodFilterInterceptor{

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		//session是否有用户存在
		if(ServletActionContext.getRequest().getSession().getAttribute("user")==null){
			//没有登录
//			ActionSupport action=(ActionSupport) invocation.getAction();
//			action.addActionError(action.getText("UserAction.relogin"));
			//跳转到登录页面
//			return "login";
			return Action.LOGIN;
		}
		
		return invocation.invoke();//放行
	}

}
