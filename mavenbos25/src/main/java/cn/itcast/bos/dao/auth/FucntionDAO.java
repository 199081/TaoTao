package cn.itcast.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;

//功能权限的dao接口
public interface FucntionDAO extends JpaRepository<Function, String>{

	public List<Function> findByFunctionIsNotNull();

	/**
	 * 
	 * 说明：
	 * @param parentFunction
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月27日 上午10:53:21
	 */
	@Query("select max(f.id) from Function f where f.function= ?")//hql
	public String findByFunciton(Function parentFunction);

	/**
	 * 
	 * 说明：查询所有的菜单-给超管用
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月27日 下午4:27:29
	 */
	@Query("from Function where generatemenu='1' order by zindex")
	public List<Function> findMenuForAdmin();

	@Query("from Function f inner join fetch f.roles r inner join fetch r.users u where u= ? and f.generatemenu='1' order by f.zindex")
	public List<Function> findMenuForUser(User user);

}
