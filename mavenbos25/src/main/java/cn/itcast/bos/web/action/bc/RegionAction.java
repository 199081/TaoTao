package cn.itcast.bos.web.action.bc;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.service.bc.RegionService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.base.BaseAction;

//区域的action
@ParentPackage("basic-bos")
@Namespace("/")
@Controller
@Scope("prototype")
public class RegionAction extends BaseAction<Region>{
	//注入Service
	@Autowired
	private RegionService regionService;
	
	//属性驱动
	private File upload;//自动（还有两个uploadFilename,uploadContentType）
	public void setUpload(File upload) {
		this.upload = upload;
	}

	//导入区域excel文件
	@Action(value="region_importData")
	public String importData(){
		//获取上传到文件（自动操作：值栈中放一个和file的name参数名字一样的属性即可）
		
		//结果map
		Map<String, Object> resultMap= new HashMap<String, Object>();
		try {
			System.out.println(upload);//文件已经在tomcat的临时文件夹中了，服务器中。
			//马上要解析excel--可能会有异常
			//定义一个list
			List<Region> regionList = new ArrayList<Region>();
			
			//1。打开excel工作簿
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(upload));
			//2.读取指定的工作表
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			//3.遍历读取每一行
			for (Row row : sheet) {
//				sheet.getFirstRowNum();
				//如果第一行，则跳过,不解析
				if(row.getRowNum()==0){
					continue;
				}
				//问题：什么时候解析完？
				//excel老版本：最大6w5行，新版本多
				//api解析到一个空行后，会不解析
				//row代表一行，cell代表一行中的一格
				if(StringUtils.isNotBlank(row.getCell(0).getStringCellValue())){
					Region region = new Region();
					//正式解析格格里面的数据
					region.setId(row.getCell(0).getStringCellValue());
					region.setProvince(row.getCell(1).getStringCellValue());
					region.setCity(row.getCell(2).getStringCellValue());
					region.setDistrict(row.getCell(3).getStringCellValue());
					region.setPostcode(row.getCell(4).getStringCellValue());
					
					//拼音的转换
					//省
					String province=row.getCell(1).getStringCellValue();
					//市
					String city=row.getCell(2).getStringCellValue();
					//区
					String district=row.getCell(3).getStringCellValue();
					
					String shortCode =province.substring(0, province.length()-1)+city.substring(0, city.length()-1)+district.substring(0, district.length()-1);
					
					//简码：省市区第一个字母
					String[] shortCodeArray = PinYin4jUtils.getHeadByString(shortCode);
					//要么你循环遍历自己组装
					shortCode = StringUtils.join(shortCodeArray, "");
					
					region.setShortcode(shortCode);
					//城市编码：拼音
					String citycode=PinYin4jUtils.hanziToPinyin(city.substring(0,city.length()-1)) ;
					region.setCitycode(citycode);
					
					regionList.add(region);
				}
			}
			//调用业务层保存数据
			regionService.saveRegionList(regionList);
			
			resultMap.put("result", true);
		} catch (Exception e) {
			resultMap.put("result", false);
			e.printStackTrace();
		}
		//压入栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		return JSON;
	}
	
	//分页查询
	@Action("region_listPage")
	public String listPage(){
		//将datagrid传过来的两个参数封装到分页请求参数对象
		Pageable pageable = new PageRequest(page-1, rows);
		
		//调用业务层
		Page<Region> page= regionService.findRegionListPage(pageable);
		//结果重新封装
		Map<String , Object> resultMap = new HashMap<String , Object>();
		resultMap.put("total", page.getTotalElements());
		resultMap.put("rows", page.getContent());
		//压入栈顶
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return JSON;
	}
	
	//属性驱动:自动封装参数到属性中
//	private int page;
//	private int rows;
//
//	public void setPage(int page) {
//		this.page = page;
//	}
	
	//列表查询
	@Action("region_list")
	public String list(){
//		List<Region> regionList= regionService.findAllRegionList();
		
		//q：下拉框自动发的参数
		String param=super.getValueFromParameter("q");
		
		List<Region> regionList= regionService.findRegionListByProvinceOrCityOrDistrict(param);
		//压入栈顶
		ActionContext.getContext().getValueStack().push(regionList);
		return JSON;
	}
	
}
