package cn.itcast.bos.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.UserDAO;
import cn.itcast.bos.dao.auth.RoleDAO;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.UserService;
import cn.itcast.bos.service.base.BaseService;
import cn.itcast.bos.utils.MD5Utils;

/**
 * 
 * 说明：用户操作的业务层接口
 * @author 传智.霹雳火
 * @version 1.0
 * @date 2016年1月30日
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseService implements UserService{
	//注入dao
	@Autowired//会在spring容器中，自动寻找UserDAO类型的bean
	//通过jpa扫描功能（需要配置），自动将默认的repository的实现类simp。。。。注入给Userdao
	private UserDAO userDAO;
	//注入角色的dao
	@Autowired
	private RoleDAO roleDAO;

	@Override
	public void saveUser(User user) {
		user.setPassword(MD5Utils.md5(user.getPassword()));
		userDAO.save(user);
	}

	@Override
	public List<User> findAllUser() {
		return userDAO.findAll();
	}

	@Override
	public User findUserByUsername(String username) {
		return userDAO.findByUsername(username);
	}

//	@Override
//	public List<User> findUserListByUsernameAndPassword(String username, String password) {
//		return userDAO.findByUsernameAndPassword(username, password);
//	}

	@Override
	public User login(String username, String password) {
		return userDAO.findByUsernameAndPassword(username, MD5Utils.md5(password));
	}

	@Override
	public String findPasswordByUsername(String username) {
		return userDAO.findPasswordByUsername(username);
	}

	@Override
	public void updateUserForPassword(String id, String password) {
		//调用dao层
		//修改：使用save方法修改，但必须保证实体都有值；快照更新，但先查询再改。
		//这里直接发语句-效率高
		userDAO.updateForPassword(id, MD5Utils.md5(password));
	}

	@Override
	public void saveUser(User user, String[] roleIds) {
		//----保存用户
		user.setPassword(MD5Utils.md5(user.getPassword()));
		userDAO.save(user);
		//----用户赋予角色（关联角色）-多表操作
		if(roleIds!=null){
			for (String roleId : roleIds) {
				//持久态
				Role role = roleDAO.findOne(roleId);
				//快照更新
				user.getRoles().add(role);
			}
		}
	}
	
	
}
