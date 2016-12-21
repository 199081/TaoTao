package cn.itcast.bos.service.qp.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cn.itcast.bos.dao.bc.DecidedZoneDAO;
import cn.itcast.bos.dao.bc.RegionDAO;
import cn.itcast.bos.dao.bc.SubareaDAO;
import cn.itcast.bos.dao.qp.NoticeBillDAO;
import cn.itcast.bos.dao.qp.WorkBillDAO;
import cn.itcast.bos.dao.ws.stub.CustomerService;
import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.domain.qp.NoticeBill;
import cn.itcast.bos.domain.qp.WorkBill;
import cn.itcast.bos.domain.ws.Customer;
import cn.itcast.bos.domain.ws.TransferRequestData;
import cn.itcast.bos.domain.ws.TransferResponseData;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.qp.NoticeBillService;

//通知单的业务层实现类
@Service
@Transactional
public class NoticeBillServiceImpl extends BaseService implements NoticeBillService{
	//注入dao
	@Autowired
	private NoticeBillDAO noticeBillDAO;
	//注入客服系统的接口桩
	@Autowired
	private CustomerService customerService;
	//注入定区的dao
	@Autowired
	private DecidedZoneDAO decidedZoneDAO;
	//注入工单的dao
	@Autowired
	private WorkBillDAO workBillDAO;
	
	//注入区域的dao
	@Autowired
	private RegionDAO regionDAO;
	//注入分区的dao
	@Autowired
	private SubareaDAO subareaDAO;

	@Override
	public void saveNoticeBill(NoticeBill noticeBill) {
		//调用dao层
		noticeBillDAO.save(noticeBill);
		//默认的通知单的类型：手工（分单）
		noticeBill.setOrdertype("手动");
		
		//客户的详细地址
		String address = noticeBill.getPickaddress();
		
		
		//------根据客户地址完全匹配定区，找到派送员，派单
		// 规则4：根据用户地址查询定区编号
		//* 参数：{'opertype':'103','param':{'address':'上海市闵行区联航路10号'}}
		 //* 返回：{'status':1,'data':[{"decidedZoneId":"DQ001"}]}
		//json解析器
		Gson gson = new GsonBuilder()     
				.excludeFieldsWithoutExposeAnnotation()//打开Expot注解，但打开了这个注解,副作用，要转换和不转换都要加注解
	            .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
	            .setPrettyPrinting() //自动格式化换行
	            .create();
		//拼装一个请求参数json
		TransferRequestData requestData = new TransferRequestData();
		requestData.setOpertype("103");//查询未关联的客户
		Map<String , String> paramMap = new HashMap<String, String>();
		paramMap.put("address", address);
		requestData.setParam(paramMap);
		//解析成json
		String paramJson = gson.toJson(requestData);
		String reusltJson = customerService.operateCustomer(paramJson);
		//判断，返回的json数据是否为空，和符合要求。
		if(StringUtils.isNotBlank(reusltJson)){
			//返回的数据-解析组装
			TypeToken<TransferResponseData<Customer>> respTypeToken = new TypeToken<TransferResponseData<Customer>>(){};
			TransferResponseData<Customer> resultData=gson.fromJson(reusltJson, respTypeToken.getType());
			
			List<Customer> data = resultData.getData();
			if(!data.isEmpty()){
				//有数据
				String decidedZoneId = data.get(0).getDecidedZoneId();
				if(StringUtils.isNotBlank(decidedZoneId)){
					//有定区,找到负责人派送员
					DecidedZone decidedZone = decidedZoneDAO.findOne(decidedZoneId);
					//负责人
					Staff staff = decidedZone.getStaff();
					this.saveWorkBill(noticeBill, staff);
					
					//通知单的：自动下单类型
					noticeBill.setOrdertype("自动");
					noticeBill.setStaff(staff);
					return;
				}
			}
		}
		
		//------根据客户地址中的关键字匹配(先匹配区域，再匹配分区关键字，找到分区，通过分区找到定区，通过定区找人)
		//目标：对用户的地址进行字符串分割，得到省市区
		//得到每一部分的索引
		//省
		int provinceIndex = address.indexOf("省");
		if(provinceIndex==-1){
			//如果是直辖市
			provinceIndex = address.indexOf("市");
		}
		//市
		int cityIndex = address.indexOf("市", provinceIndex+1);
		//区
		int districtIndex = address.indexOf("区", cityIndex+1);
		//切。。。。字段的条件
		String province = address.substring(0, provinceIndex+1);//省
		String city=address.substring(provinceIndex+1, cityIndex+1);//市
		String district=address.substring(cityIndex+1, districtIndex+1);//区
		//调用dao查询了（条件就是省市区）
		Region region= regionDAO.findByProvinceAndCityAndDistrict(province,city,district);
		//如果能找到该区域
		if(null!=region){
			//根据区域id查询下面的分区的列表
			List<Subarea> subareaList= subareaDAO.findByRegion(region);
			//如果该区域有分区
			if(!subareaList.isEmpty()){
				
				for (Subarea subarea : subareaList) {
					//过滤寻找关键字
					if(address.contains(subarea.getAddresskey())){
						//如果用户地址包含分区的关键字，说明找到这个分区,从而获取定区
						DecidedZone decidedZone = subarea.getDecidedZone();//多表导航查询
						//如果该分区已经定区了
						if(null!=decidedZone){
							//获取了取派员
							Staff staff = decidedZone.getStaff();
							this.saveWorkBill(noticeBill, staff);
							
							//通知单：自动下单类型
							noticeBill.setOrdertype("自动");
							noticeBill.setStaff(staff);
							return;
						}
						
					}
					
				}
			}
		}
		
	}

	//保存工单
	public void saveWorkBill(NoticeBill noticeBill, Staff staff) {
		//组装工单
		WorkBill workBill = new WorkBill();
		workBill.setAttachbilltimes(new BigDecimal(0));//追单次数，开始肯定是0
		workBill.setBuildtime(new Date());//工单的创建时间，默认当前时间
		workBill.setNoticeBill(noticeBill);//通知单的外键，必须保证有主键id
		workBill.setPickstate("新单");//取件状态
		workBill.setRemark(noticeBill.getRemark());//备注，业务要求,从通知单里面来
		workBill.setStaff(staff);//派送员负责人，必须保证有主键即可
		workBill.setType("新");//工单类型，默认是新
		//调用工单的dao，保存工单
		workBillDAO.save(workBill);
		//给派送员发送短信
		///略。。。。send（）
		//
	}

}
