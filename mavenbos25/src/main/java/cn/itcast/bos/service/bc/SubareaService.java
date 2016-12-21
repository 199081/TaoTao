package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.bc.Subarea;

//分区的业务层接口
public interface SubareaService {

	/**
	 * 保存分区
	 * 说明：
	 * @param subarea
	 * @author 传智.BoBo老师
	 * @time：2016年2月19日 下午3:50:00
	 */
	public void saveSubarea(Subarea subarea);

	/**
	 * 
	 * 说明：分页条件查询分区
	 * @param specification
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月19日 下午5:31:20
	 */
	public Page<Subarea> findSubareaListPage(Specification<Subarea> specification, Pageable pageable);

	/**
	 * 
	 * 说明：根据条件查询分区列表
	 * @param specification
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 上午9:38:25
	 */
	public List<Subarea> findSubareaList(Specification<Subarea> specification);

	/**
	 * 
	 * 说明：查询没有定区的分区（没有定区外键的id）
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 上午10:33:36
	 */
	public List<Subarea> findSubareaListNoDecideZone();

}
