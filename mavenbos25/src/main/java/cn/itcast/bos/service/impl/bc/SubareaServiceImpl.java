package cn.itcast.bos.service.impl.bc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.SubareaDAO;
import cn.itcast.bos.domain.bc.Subarea;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.bc.SubareaService;
//分区的业务层实现类
@Service("subareaService")
@Transactional
public class SubareaServiceImpl extends BaseService implements SubareaService {
	//注入dao
	@Autowired
	private SubareaDAO subareaDAO;

	@Override
	public void saveSubarea(Subarea subarea) {
		//调用dao
		subareaDAO.save(subarea);
	}

	@Override
	public Page<Subarea> findSubareaListPage(Specification<Subarea> specification, Pageable pageable) {
		return subareaDAO.findAll(specification, pageable);
	}

	@Override
	public List<Subarea> findSubareaList(Specification<Subarea> specification) {
		return subareaDAO.findAll(specification);
	}

	@Override
	public List<Subarea> findSubareaListNoDecideZone() {
		//属性表达式
		 return subareaDAO.findByDecidedZoneIsNull();
		//query的注解
//		 return subareaDAO.findListNoDecideZone();
	}

}
