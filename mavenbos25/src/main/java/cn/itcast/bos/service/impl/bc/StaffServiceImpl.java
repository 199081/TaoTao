package cn.itcast.bos.service.impl.bc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.bc.StaffDAO;
import cn.itcast.bos.domain.bc.Staff;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.service.bc.StaffService;

//取派员管理的业务层实现类
@Service
@Transactional
public class StaffServiceImpl extends BaseService implements StaffService{
	//注入dao
	@Autowired
	private StaffDAO staffDAO;

	@Override
	public void saveStaff(Staff staff) {
		staffDAO.save(staff);
	}

	@Override
	public Page<Staff> findStaffListPage(Pageable pageable) {
		//调用dao层查询
		return staffDAO.findAll(pageable);
	}

	@Override
	public void deleteStaffBatch(String ids) {
		//先切
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			//调用dao层
			staffDAO.updateStaffDeltagById(id);
		}
		
		
	}

	@Override
	public List<Staff> findStaffListNoDeltag() {
		return staffDAO.findByDeltag('0');
	}

}
