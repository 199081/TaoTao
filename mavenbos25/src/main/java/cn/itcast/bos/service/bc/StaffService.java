package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.bc.Staff;

//取派员操作的业务层
public interface StaffService {

	/**
	 * 保存员工
	 * 说明：
	 * @param staff
	 * @author 传智.BoBo老师
	 * @time：2016年2月18日 上午11:58:32
	 */
	public void saveStaff(Staff staff);

	/**
	 * 
	 * 说明：分页列表查询快递员
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月18日 下午3:36:23
	 */
	public Page<Staff> findStaffListPage(Pageable pageable);

	/**
	 * 
	 * 说明：批量删除（逻辑删除）派送员
	 * @param ids
	 * @author 传智.BoBo老师
	 * @time：2016年2月18日 下午4:47:37
	 */
	public void deleteStaffBatch(String ids);

	/**
	 * 
	 * 说明：查询未作废的派送员
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 上午10:24:05
	 */
	public List<Staff> findStaffListNoDeltag();

}
