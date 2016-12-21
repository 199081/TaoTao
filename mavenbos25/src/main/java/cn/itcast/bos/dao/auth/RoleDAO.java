package cn.itcast.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
//角色dao层接口
public interface RoleDAO extends JpaRepository<Role, String> {

	/**
	 * 
	 * 说明：根据用户查询权限列表
	 * @param user
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月25日 下午5:21:32
	 */
	public List<Role> findByUsers(User user);

}
