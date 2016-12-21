package cn.itcast.bos.service.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.bc.Region;

//区域相关的业务层接口
public interface RegionService {

	/**
	 * 
	 * 说明：批量保存
	 * @param regionList
	 * @author 传智.BoBo老师
	 * @time：2016年2月19日 上午10:58:17
	 */
	public void saveRegionList(List<Region> regionList);

	/**
	 * 
	 * 说明：分页查询区域
	 * @param pageable
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月19日 下午2:44:03
	 */
	public Page<Region> findRegionListPage(Pageable pageable);

	/**
	 * 
	 * 说明：查询所有区域列表
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月19日 下午3:00:42
	 */
	public List<Region> findAllRegionList();

	/**
	 * 
	 * 说明：根据省市区查询区域
	 * @param param
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月19日 下午3:17:36
	 */
	public List<Region> findRegionListByProvinceOrCityOrDistrict(String param);

}
