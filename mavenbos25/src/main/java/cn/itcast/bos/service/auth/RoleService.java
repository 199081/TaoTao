package cn.itcast.bos.service.auth;

import java.util.List;

import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;

//角色业务层
public interface RoleService {

	/**
	 * 根据用户查询出其拥有的角色
	 * 说明：
	 * @param user
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月25日 下午5:16:29
	 */
	public List<Role> findRoleListByUser(User user);

	/**
	 * 
	 * 说明：查询所有角色列表
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月27日 上午11:31:33
	 */
	public List<Role> findRoleList();

	/**
	 * 
	 * 说明：保存角色并关联权限（赋权）
	 * @param role
	 * @param functionIds
	 * @author 传智.BoBo老师
	 * @time：2016年2月27日 下午2:39:04
	 */
	public void saveRole(Role role,String functionIds);


}
