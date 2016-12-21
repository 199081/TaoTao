package cn.itcast.bos.service;

import java.util.List;

import cn.itcast.bos.domain.user.User;

/**
 * 
 * 说明：用户操作的业务层接口
 * @author 传智.霹雳火
 * @version 1.0
 * @date 2016年1月30日
 */
public interface UserService {
	//定义业务方法
	/**
	 * 说明：保存用户
	 * @param user
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 上午11:31:54
	 */
	public void saveUser(User user);
	
	/**
	 * 
	 * 说明：查询所有用户
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 上午11:32:56
	 */
	public List<User> findAllUser();
	//根据用户名查询用户
	public User findUserByUsername(String username);
	//根据用户名和密码两个条件查询用户列表
//	public List<User> findUserListByUsernameAndPassword(String username,String password);
	/**
	 * 
	 * 说明：登录
	 * @param username
	 * @param password
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 下午3:06:19
	 */
	public User login(String username,String password);
	
	//根据用户名查询密码
	public String findPasswordByUsername(String username);

	/**
	 * 
	 * 说明：修改当前用户的密码
	 * @param id
	 * @param password
	 * @author 传智.BoBo老师
	 * @time：2016年2月16日 下午3:09:12
	 */
	public void updateUserForPassword(String id, String password);

	/**
	 * 
	 * 说明：保存用户并关联角色
	 * @param user
	 * @param roleIds
	 * @author 传智.BoBo老师
	 * @time：2016年2月27日 下午3:16:47
	 */
	public void saveUser(User user, String[] roleIds);
}
