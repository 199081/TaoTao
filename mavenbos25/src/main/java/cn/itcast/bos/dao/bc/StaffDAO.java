package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.Staff;

//取派员的dao层
public interface StaffDAO extends JpaRepository<Staff, String> {

	@Modifying//更新操作
	@Query("update Staff set deltag = 1 where id =?")//hql
	public void updateStaffDeltagById(String id);

	/**
	 * 
	 * 说明：根据删除标记查询派送员
	 * @param c
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 上午10:25:45
	 */
	public List<Staff> findByDeltag(Character deltag);
	
	

}
