package cn.itcast.bos.web.action.qp;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.qp.NoticeBillService;
import cn.itcast.bos.web.base.BaseAction;

//业务提醒受理单action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller
@Scope("prototype")
public class NoticeBillAction extends BaseAction<NoticeBill>{
	//注入业务层
	@Autowired
	private NoticeBillService noticeBillService;

	//新单（保存+自动派单）
	@Action(value="noticeBill_save",results ={@Result(name=SUCCESS,location="/WEB-INF/pages/qupai/noticebill_add.jsp")})
	public String save(){
		//受理人
		User loginUser=(User) super.getValueFromSession("user");
		model.setUser(loginUser);
		//调用业务层
		noticeBillService.saveNoticeBill(model);
		return SUCCESS;
	}
}
