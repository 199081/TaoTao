package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.ws.Customer;

//定区的业务层接口
public interface DecidedZoneService {

	/**
	 * 
	 * 说明：保存定区（定区关联分区）
	 * //思考：操作两张表：一张定区+更新分区的定区外键字段
	 * @param decidedZone
	 * @param subareaId
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 上午11:24:55
	 */
	public void saveDecidedZone(DecidedZone decidedZone, String[] subareaId);

	/**
	 * 
	 * 说明：条件分页查询定区
	 * @param specification
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 下午3:14:53
	 */
	public Page<DecidedZone> findDecidedZoneListPage(Specification<DecidedZone> specification, Pageable pageable);
	
	//查询没有定区的客户
	public List<Customer> findCustomerListNoDecidedZone();
	//查询有当前定区的客户
	public List<Customer> findCustomerListHasDecidedZone(String decidedZoneId);
	//客户关联定区
	public void associateCustomerToDecidedZone(String decidedZoneId,String customerIds);
	
	
	
	
	

}
