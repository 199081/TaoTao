package cn.itcast.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.bc.DecidedZone;
import cn.itcast.bos.domain.bc.Region;
import cn.itcast.bos.domain.bc.Subarea;

//分区的dao层
public interface SubareaDAO extends JpaRepository<Subarea, String>,JpaSpecificationExecutor<Subarea> {

	/**
	 * 
	 * 说明：查询没有定区的分区
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 上午10:37:34
	 */
	public List<Subarea> findByDecidedZoneIsNull();

	/**
	 * 
	 * 说明：查询没有定区的分区
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 上午10:37:34
	 */
	@Query("from Subarea where decidedZone is null")//hql,面向对象
	public List<Subarea> findListNoDecideZone();

	/**
	 * 
	 * 说明：更新分区的定区字段
	 * @param decidedZone
	 * @param id
	 * @author 传智.BoBo老师
	 * @time：2016年2月21日 上午11:37:04
	 */
	@Modifying
	@Query("update Subarea set decidedZone= ? where id =?")
	public void updateForDecidedZone(DecidedZone decidedZone,String id);

	/**
	 * 
	 * 说明：根据区域查询下属的分区
	 * @param region
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月24日 上午10:26:39
	 */
	public List<Subarea> findByRegion(Region region);


}
