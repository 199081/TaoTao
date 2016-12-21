package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cn.itcast.bos.domain.user.User;

//DAO(Repository):
//spring data jpa开发只需要定义接口即可，实现类内部有实现,必须继续一个接口
//无需加bean的注解
public interface UserDAO extends JpaRepository<User, String>{
	
	/**
	 * 根据用户名查询用户
	 * 说明：where username =?
	 * @param username
	 * @return   会根据类型自动封装，如果是User单对象，会自动封装为user对象，如果是List<User>，会自动封装为user list
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 下午2:56:54
	 */
	public User findByUsername(String username);
	/**
	 * 
	 * 说明：根据用户名密码查询用户列表，相当于：where username =? and password =?
	 * @param username
	 * @param password
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 下午2:58:53
	 */
//	public List<User> findByUsernameAndPassword(String username,String password);
	
	/**
	 * 
	 * 说明：登录：根据用户名和密码查询
	 * @param username
	 * @param password
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 下午3:07:03
	 */
	public User findByUsernameAndPassword(String username,String password);
	
	//根据用户名查询密码
	//命名查询的名字必须是实体类.方法名
//	public String findPasswordByUsername(String username);
	
	/**
	 * 使用spring Data JPA的JPQL语句进行查询
	 * 说明：
	 * @param username
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 下午3:23:25
	 */
	//@Query("select u.password from User u where u.username =?")//hql
//	@Query(value="select password from t_user where username =?",nativeQuery=true)//sql
//	public String findPasswordByUsername(String username);
	
	//命名参数查询
	@Query(value="select password from t_user where username =:username",nativeQuery=true)//sql
	public String findPasswordByUsername(@Param("username")String username);

	/**
	 * 
	 * 说明：修改个人密码
	 * @param id
	 * @param password
	 * @author 传智.BoBo老师
	 * @time：2016年2月16日 下午3:12:33
	 */
	@Modifying//告诉Query接口可以执行增删改
//	@Query("update User set password = :password where id =:id")//底层认为就是Query接口，也可以执行增删改
	@Query(value="update t_user set password =:password where id = :id" ,nativeQuery=true)
	public void updateForPassword(@Param("id")String id, @Param("password")String password);
	

}
