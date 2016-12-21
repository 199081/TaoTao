package cn.itcast.bos.service.qp.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.qp.WorkOrderManageDAO;
import cn.itcast.bos.domain.qp.WorkOrderManage;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.qp.WorkOrderManageService;

//工作单管理业务层实现类
@Service
@Transactional
public class WorkOrderManageServiceImpl extends BaseService implements WorkOrderManageService {
	//注入dao
	@Autowired
	private WorkOrderManageDAO workOrderManageDAO;
	@Override
	public void saveWorkOrderManage(WorkOrderManage workOrderManage) {
		//调用到层
		workOrderManageDAO.save(workOrderManage);
	}
	@Override
	public Page<WorkOrderManage> findWorkOrderManageList(Pageable pageable) {
		return workOrderManageDAO.findAll(pageable);
	}
	@Override
	public Page<WorkOrderManage> findWorkOrderManageList(Pageable pageable, String conditionName,
			String conditionValue) {
		//调用dao:hibernate Search(数据层)
		return workOrderManageDAO.searchByLucence(pageable,conditionName,conditionValue);
	}

}
