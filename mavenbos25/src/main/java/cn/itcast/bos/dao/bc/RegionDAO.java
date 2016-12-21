package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.Region;

//区域的dao接口
public interface RegionDAO extends JpaRepository<Region, String> {

	/**
	 * 
	 * 说明：根据省市区查询区域
	 * @param param
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月19日 下午3:18:43
	 */
	@Query("from Region where province like ?1 or city like ?1 or district like ?1 or shortcode like ?1 or citycode like ?1")
	public List<Region> findListByProvinceOrCityOrDistrict(String param);

	/**
	 * 根据省市区完全匹配的条件进行查询区域
	 * 说明：
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月24日 上午10:21:48
	 */
	public Region findByProvinceAndCityAndDistrict(String province, String city, String district);


}
