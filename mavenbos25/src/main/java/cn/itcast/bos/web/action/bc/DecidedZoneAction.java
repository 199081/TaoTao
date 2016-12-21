package cn.itcast.bos.web.action.bc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.ws.Customer;
import cn.itcast.bos.service.bc.DecidedZoneService;
import cn.itcast.bos.web.base.BaseAction;

//定区的action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller
@Scope("prototype")
public class DecidedZoneAction extends BaseAction<DecidedZone> {
	//注入serivce
	@Autowired
	private DecidedZoneService decidedZoneService;
	
	//属性驱动，来封装多个同名参数：数组，也可以是字符串逗号分割
	private String[] subareaId;
	public void setSubareaId(String[] subareaId) {
		this.subareaId = subareaId;
	}

	//保存定区
	@Action(value="decidedZone_save" ,results={@Result(name=SUCCESS,location="/WEB-INF/pages/base/decidedzone.jsp")})
	public String save(){
		//保存-调用业务层
		//思考：操作两张表：一张定区+更新分区的定区外键字段
		decidedZoneService.saveDecidedZone(model,subareaId);
		return SUCCESS;//jsp
	}
	
	//属性驱动获取否关联分区
	private String hasSubarea;
	public void setHasSubarea(String hasSubarea) {
		this.hasSubarea = hasSubarea;
	}
	
	//分页条件查询
	@Action("decidedZone_listPage")
	public String listPage(){
		//进行查询
		//构建两个：1：分页对象，2条件规范对象
		//----1：分页对象
		Pageable pageable = new PageRequest(page-1, rows);
		//----2条件规范对象
		Specification<DecidedZone> specification = new Specification<DecidedZone>() {
			
			@Override
			//返回Predicate判断条件对象
			public Predicate toPredicate(Root<DecidedZone> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//关联对象
				Predicate andPredicate = cb.conjunction();//集合：And集合
				Predicate orPredicate = cb.disjunction();//Or集合，放到这里的条件对象，都是or
				//   （  and  ）  and (  or)
				//----单表条件
				if(StringUtils.isNotBlank( model.getId())){
					andPredicate.getExpressions().add(
							cb.equal(root.get("id").as(String.class), model.getId())
							);
				}
				//----多表
				//+++++关联员工表:多对一
				if(model.getStaff()!=null){
					Join<DecidedZone, ?> staffJoin = root.join(root.getModel().getSingularAttribute("staff"), JoinType.LEFT);
					if(StringUtils.isNotBlank(model.getStaff().getStation())){
						andPredicate.getExpressions().add(
								cb.like(staffJoin.get("station").as(String.class), "%"+model.getStaff().getStation()+"%")
								);
					}
				}
				//+++++关联分区表：一对多
				if(StringUtils.isNotBlank(hasSubarea)){
					if(hasSubarea.equals("1")){
						andPredicate.getExpressions().add(
								cb.isNotEmpty(root.get("subareas").as(Set.class))
								);
					}else{
						andPredicate.getExpressions().add(
								cb.isEmpty(root.get("subareas").as(Set.class))
								);
					}
				}
				
				return andPredicate;
			}
		};
		
		//调用业务层查询
		Page<DecidedZone> page= decidedZoneService.findDecidedZoneListPage(specification,pageable);
		//重新组装数据
		Map<String , Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", page.getContent());
		
		//放入值栈
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
	}
	
	//获取未关联定区的客户（远程）
	@Action("decidedZone_listCustomerNoDecidedZone")
	public String listCustomerNoDecidedZone(){
		//调用业务层
		List<Customer> list = decidedZoneService.findCustomerListNoDecidedZone();
		//压入栈顶
		ActionContext.getContext().getValueStack().push(list);
		return JSON;
	}
	//获取已经关联定区的客户（远程）
	@Action("decidedZone_listCustomerHasDecidedZone")
	public String listCustomerHasDecidedZone(){
		//调用业务层
		List<Customer> list = decidedZoneService.findCustomerListHasDecidedZone(model.getId());
		//压入栈顶
		ActionContext.getContext().getValueStack().push(list);
		return JSON;
	}
	
	@Action(value="decidedzone_assigncustomerstodecidedzone",results={@Result(name=SUCCESS,location="/WEB-INF/pages/base/decidedzone.jsp")})
	public String decidedzone_assigncustomerstodecidedzone(){
		//重新封装数据
//		Arrays.to
		String cIds = StringUtils.join(customerIds, ",");
		//调用业务层
		decidedZoneService.associateCustomerToDecidedZone(model.getId(), cIds);
		
		return SUCCESS;
	}
	
	//属性驱动封装选中的客户（, a, a）
	private String[] customerIds;
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	
	
}
