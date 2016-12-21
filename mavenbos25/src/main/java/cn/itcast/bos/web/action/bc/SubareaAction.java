package cn.itcast.bos.web.action.bc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.struts2.ServletActionContext;
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
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.service.bc.SubareaService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.web.base.BaseAction;

//分区的action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubareaAction extends BaseAction<Subarea>{
	//注入业务层
//	@Autowired
	private SubareaService subareaService;
	//setter方法属性直接注入
	@Autowired
	public void setSubareaService(SubareaService subareaService){
		this.subareaService=subareaService;
	}

	//保存分区
	@Action(value="subarea_save",results={@Result(name=SUCCESS,location="/WEB-INF/pages/base/subarea.jsp")})
	@RequiresRoles(value="weihu1")//方法可以同时设置多个角色
	public String save(){
		//直接调用业务层
		subareaService.saveSubarea(model);
		
		return SUCCESS;
	}
	
	
	//分页条件查询分区列表
	@Action("subarea_listPage")
	public String listPage(){
		//分页请求对象
		Pageable pageable = new PageRequest(page-1, rows);
		//规范条件对象Specification
		Specification<Subarea> specification = getSubareaSpecification();
		
		//调用业务层查询数据
//		Page<T> page=subareaService.findSubareaListPage(Specification<T> spec, Pageable pageable);
		Page<Subarea> page=subareaService.findSubareaListPage(specification, pageable);
		
		//重新组装数据
		Map<String , Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", page.getContent());
		
		//压入栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
	}


	private Specification<Subarea> getSubareaSpecification() {
		//root根查询（主）对象类型//root根查询（主）对象类型
		Specification<Subarea> specification =new Specification<Subarea>() {
			@Override
			//参数1：Root：根查询对象-主对象
			//参数2：CriteriaQuery：jpa的查询接口
			//参数3：CriteriaBuilder：jpa的criteria的构造器
			public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//可以声明一个list，存放条件对象
				List<Predicate> pAndList =new ArrayList<Predicate>();
				//--------------------单表条件
				//关键字
				if(StringUtils.isNotBlank(model.getAddresskey())){
					//参数1：表达式（属性），参数2：值
					//root.get(对象属性的名字)
					//as属性的数据类型
					Predicate p1 = cb.like(root.get("addresskey").as(String.class), "%"+model.getAddresskey()+"%");
					//restricons.add(resu..lk)
					pAndList.add(p1);
					
				}
				//定区编号
				if(model.getDecidedZone()!=null && StringUtils.isNotBlank(model.getDecidedZone().getId())){
//					Predicate p2 = cb.equal(root.get("decidedZone"), model.getDecidedZone());//反射机制
					Predicate p2 = cb.equal(root.get("decidedZone").as(DecidedZone.class), model.getDecidedZone());
					pAndList.add(p2);
				}
				
				//--------------------多表条件
				//定区表的条件（关联查询）
				if(model.getRegion()!=null){
					//关联对象（相当于criteria的子查询对象）
					//默认内连接
//					Join<Subarea, ?> regionJoin = root.join(root.getModel().getSingularAttribute("region"));
					Join<Subarea, ?> regionJoin = root.join(root.getModel().getSingularAttribute("region"),JoinType.LEFT);
					//省
					if(StringUtils.isNotBlank(model.getRegion().getProvince())){
						Predicate p3 = cb.like(regionJoin.get("province").as(String.class),"%"+ model.getRegion().getProvince()+"%");
						pAndList.add(p3);
					}
					//市
					if(StringUtils.isNotBlank(model.getRegion().getCity())){
						Predicate p4 = cb.like(regionJoin.get("city").as(String.class), "%"+model.getRegion().getCity()+"%");
						pAndList.add(p4);
					}
					//区
					if(StringUtils.isNotBlank(model.getRegion().getDistrict())){
						Predicate p5 = cb.like(regionJoin.get("district").as(String.class), "%"+model.getRegion().getDistrict()+"%");
						pAndList.add(p5);
					}
				}
				//无需关心语句如何拼写。
				//小知识点：list转换为数组
				Predicate[] array = pAndList.toArray(new Predicate[0]);
//				Predicate pp1 = cb.and(array);
//				Predicate pp2 = cb.or(array);
				
				return cb.and(array);
			}
		};
		return specification;
	}
	
	//导出分区
	@Action(value="subarea_export")
	public String export() throws Exception{
		//---封装获取specification条件规范对象
		//规范条件对象Specification
		Specification<Subarea> specification = getSubareaSpecification();
		
		//---查询数据库的数据，生成数据列表
		List<Subarea> sList= subareaService.findSubareaList(specification);
		//----解析数据生成Excel文件
		//记忆方法：跟你自己写一个excel文件的方式一样
		//1.创建一个excel工作簿文件
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		//2.在工作簿中创建一个工作表sheet
		//HSSFSheet sheet = hssfWorkbook.createSheet();
		HSSFSheet sheet = hssfWorkbook.createSheet("分区列表");
		//3.一行一行写数据
		//3.1写第一行，标题:分区编号	区域编码	关键字	起始号	结束号	单双号	位置信息
		HSSFRow headerRow = sheet.createRow(0);
		//写格格
		headerRow.createCell(0).setCellValue("分区编号");
		headerRow.createCell(1).setCellValue("区域编码");
		headerRow.createCell(2).setCellValue("关键字");
		headerRow.createCell(3).setCellValue("起始号");
		headerRow.createCell(4).setCellValue("结束号");
		headerRow.createCell(5).setCellValue("单双号");
		headerRow.createCell(6).setCellValue("位置信息");
		//3.2下面的内容
		for (Subarea subarea : sList) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum()+1);
			//写格格
			dataRow.createCell(0).setCellValue(subarea.getId());
			dataRow.createCell(1).setCellValue(subarea.getRegion().getId());
			dataRow.createCell(2).setCellValue(subarea.getAddresskey());
			dataRow.createCell(3).setCellValue(subarea.getStartnum());
			dataRow.createCell(4).setCellValue(subarea.getEndnum());
			dataRow.createCell(5).setCellValue(subarea.getSingle().toString());//char类型的数据问题
			dataRow.createCell(6).setCellValue(subarea.getPosition());
		}
		
		//-----下载
		//将excel写入响应
//		hssfWorkbook.write(ServletActionContext.getResponse().getOutputStream());
		//两个
		//设置客户端浏览器用于识别附件的两个参数Content-Type和Content-Disposition
		//文件名
		String downFilename="分区数据.xls";
		//获取文件的MIME类型：
		String contentType=ServletActionContext.getServletContext().getMimeType(downFilename);
		//将MIME类型放入响应
		ServletActionContext.getResponse().setContentType(contentType);
		//浏览器类型
		String agent = ServletActionContext.getRequest().getHeader("user-agent");
		//附件名编码，解决中文乱码问题
		downFilename = FileUtils.encodeDownloadFilename(downFilename, agent);
		//获取附件的名字和下载方式
		String contentDisposition="attachment;filename="+downFilename;
		//将附件名字和下载方式放入响应头信息中
		ServletActionContext.getResponse().setHeader("Content-Disposition", contentDisposition);
		
		//将excel写入响应
		hssfWorkbook.write(ServletActionContext.getResponse().getOutputStream());
		
		//这里没有使用struts2的结果集类型stream，而是直接使用web的方式
		return NONE;//没有响应的页面
	}
	
	@Action("subarea_listNoDecideZone")
	public String listNoDecideZone(){
		//调用业务层
		List<Subarea> sList= subareaService.findSubareaListNoDecideZone();
		//压入栈顶
		ActionContext.getContext().getValueStack().push(sList);
		return JSON;
	}
}
