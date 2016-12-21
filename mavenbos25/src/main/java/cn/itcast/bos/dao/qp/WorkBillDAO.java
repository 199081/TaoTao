package cn.itcast.bos.dao.qp;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.qp.WorkBill;

//工单的dao接口
public interface WorkBillDAO extends JpaRepository<WorkBill, String>{

}
