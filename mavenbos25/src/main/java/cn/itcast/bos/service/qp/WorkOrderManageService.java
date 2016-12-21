package cn.itcast.bos.service.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.qp.WorkOrderManage;

//工作单的业务层
public interface WorkOrderManageService {

	/**
	 * 
	 * 说明：保存工作单（快速录入）
	 * @param workOrderManage
	 * @author 传智.BoBo老师
	 * @time：2016年2月24日 下午2:56:31
	 */
	
	public void saveWorkOrderManage(WorkOrderManage workOrderManage);

	/**
	 * 
	 * 说明：没有业务条件的分页查询
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月24日 下午5:05:45
	 */
	
	public Page<WorkOrderManage> findWorkOrderManageList(Pageable pageable);

	/**
	 * 
	 * 说明：条件分页查询-使用了全文搜索引擎
	 * @param pageable
	 * @param conditionName
	 * @param conditionValue
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月24日 下午5:12:31
	 */
	public Page<WorkOrderManage> findWorkOrderManageList(Pageable pageable, String conditionName,
			String conditionValue);
	
}
