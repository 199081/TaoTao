package cn.itcast.bos.dao.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.qp.WorkOrderManage;

//工作单管理的dao层
public interface WorkOrderManageDAO extends JpaRepository<WorkOrderManage, String> {

	public Page<WorkOrderManage> searchByLucence(Pageable pageable, String conditionName,
			String conditionValue);

}
