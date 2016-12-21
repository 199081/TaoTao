package cn.itcast.bos.service.impl.bc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.itcast.bos.dao.bc.DecidedZoneDAO;
import cn.itcast.bos.dao.bc.SubareaDAO;
import cn.itcast.bos.dao.ws.stub.CustomerService;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.domain.ws.Customer;
import cn.itcast.bos.domain.ws.TransferRequestData;
import cn.itcast.bos.domain.ws.TransferResponseData;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.bc.DecidedZoneService;
import cn.itcast.bos.web.base.BaseAction;

//定区操作的业务层实现类
@Service
@Transactional
public class DecidedZoneServiceImpl extends BaseService implements DecidedZoneService{
	//注入定区dao
	@Autowired
	private DecidedZoneDAO decidedZoneDAO;
	//注入区域dao
	@Autowired
	private SubareaDAO subareaDAO;
	//注入Webservice的桩
	@Autowired
	private CustomerService customerService;

	@Override
	public void saveDecidedZone(DecidedZone decidedZone, String[] subareaId) {
		//调用dao
		//1。保存定区
		decidedZoneDAO.save(decidedZone);
		
		//2。更新关联区域
		if(subareaId!=null){
			//关联--更新区域的定区字段
			
			for (String id : subareaId) {
				//1。快照更新（先查出来，再设置属性）--critieral
//				Subarea subarea = subareaDAO.findOne(id);
//				subarea.setDecidedZone(decidedZone);
				//2.直接发出更新语句
				subareaDAO.updateForDecidedZone(decidedZone,id);
			}
			
		}
		
	}

	@Override
	public Page<DecidedZone> findDecidedZoneListPage(Specification<DecidedZone> specification, Pageable pageable) {
		return decidedZoneDAO.findAll(specification, pageable);
	}

	@Override
	public List<Customer> findCustomerListNoDecidedZone() {
		//json解析器
		Gson gson = new GsonBuilder()     
				.excludeFieldsWithoutExposeAnnotation()//打开Expot注解，但打开了这个注解,副作用，要转换和不转换都要加注解
	            .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
	            .setPrettyPrinting() //自动格式化换行
	            .create();
		//拼装一个请求参数json
		TransferRequestData requestData = new TransferRequestData();
		requestData.setOpertype("101");//查询未关联的客户
		//解析成json
		String paramJson = gson.toJson(requestData);
		String reusltJson = customerService.operateCustomer(paramJson);
		//判断，返回的json数据是否为空，和符合要求。
		if(StringUtils.isNotBlank(reusltJson)){
			//返回的数据-解析组装
			TypeToken<TransferResponseData<Customer>> respTypeToken = new TypeToken<TransferResponseData<Customer>>(){};
			TransferResponseData<Customer> resultData=gson.fromJson(reusltJson, respTypeToken.getType());
			
			return resultData.getData();
		}else{
			return null;
		}
		
	}

	@Override
	// 参数：{'opertype'：'102','param':{'decidedZoneId':'DQ001'}} 
	public List<Customer> findCustomerListHasDecidedZone(String decidedZoneId) {
		//json解析器
			Gson gson = new GsonBuilder()     
					.excludeFieldsWithoutExposeAnnotation()//打开Expot注解，但打开了这个注解,副作用，要转换和不转换都要加注解
		            .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
		            .setPrettyPrinting() //自动格式化换行
		            .create();
			//拼装一个请求参数json
			TransferRequestData requestData = new TransferRequestData();
			requestData.setOpertype("102");//查询未关联的客户
			Map<String , String> paramMap = new HashMap<String, String>();
			paramMap.put("decidedZoneId", decidedZoneId);
			requestData.setParam(paramMap);
			//解析成json:{'opertype'：'102','param':{'decidedZoneId':'DQ001'}} 
			String paramJson = gson.toJson(requestData);
			//调用ws接口：回客户列表{'status':'1',data[{},{},{}]}
			String reusltJson = customerService.operateCustomer(paramJson);
			//判断，返回的json数据是否为空，和符合要求。
			if(StringUtils.isNotBlank(reusltJson)){
				//返回的数据-解析组装
				TypeToken<TransferResponseData<Customer>> respTypeToken = new TypeToken<TransferResponseData<Customer>>(){};
				TransferResponseData<Customer> resultData=gson.fromJson(reusltJson, respTypeToken.getType());
				
				return resultData.getData();
			}else{
				return null;
			}
		
	}

	@Override
	//{'opertype'：'201','param':{'decidedZoneId':'DQ001'},'customerIds':'1,2,3'}
	public void associateCustomerToDecidedZone(String decidedZoneId, String customerIds) {
		//json解析器
		Gson gson = new GsonBuilder()     
				.excludeFieldsWithoutExposeAnnotation()//打开Expot注解，但打开了这个注解,副作用，要转换和不转换都要加注解
	            .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
	            .setPrettyPrinting() //自动格式化换行
	            .create();
		
		//拼装一个请求参数json
		TransferRequestData requestData = new TransferRequestData();
		requestData.setOpertype("201");//查询未关联的客户
		Map<String , String> paramMap = new HashMap<String, String>();
		paramMap.put("decidedZoneId", decidedZoneId);
		paramMap.put("customerIds", customerIds);
		requestData.setParam(paramMap);
		//解析成json:{'opertype'：'201','param':{'decidedZoneId':'DQ001'},'customerIds':'1,2,3'}
		String paramJson = gson.toJson(requestData);
		//调用ws接口
		String reusltJson = customerService.operateCustomer(paramJson);
		if(StringUtils.isNotBlank(reusltJson)){
			//返回的数据-解析组装
			TypeToken<TransferResponseData<Customer>> respTypeToken = new TypeToken<TransferResponseData<Customer>>(){};
			TransferResponseData<Customer> resultData=gson.fromJson(reusltJson, respTypeToken.getType());
		}else{
			//返回的数据异常，失败了
			new RuntimeException("接口操作失败：关联用户不成功！");
		}
	}

}
