package cn.itcast.bos.service.auth.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.FucntionDAO;
import cn.itcast.bos.dao.auth.RoleDAO;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.auth.RoleService;
import cn.itcast.bos.service.base.BaseService;
//角色的业务层实现类
@Service
@Transactional
public class RoleServiceImpl extends BaseService implements RoleService{
	//注入dao
	@Autowired
	private RoleDAO roleDAO;
	//注入权限的dao
	@Autowired
	private FucntionDAO functionDAO;

	@Override
	public List<Role> findRoleListByUser(User user) {
		//调用dao
		//如果是超管，则拥有所有角色（应该给一个超管的角色）--bug
		if(user.getUsername().equals("admin")){
			return roleDAO.findAll();
		}else{
			return roleDAO.findByUsers(user);
		}
	}

	@Override
	public List<Role> findRoleList() {
		return roleDAO.findAll();
	}

	@Override
	public void saveRole(Role role, String functionIds) {
		//---保存角色
		roleDAO.save(role);//持久态
		
		//---角色和权限的关系保存（多对多）
		if(StringUtils.isNotBlank(functionIds)){
			//如果关联了权限
			//回忆：多对多的关系怎么建立？
			//一方关联另外一方就可以了（必须是持久态），自动往中间表放关系数据
			//切割字符串为数组
			String[] functionIdArray = functionIds.split(",");
			for (String functionId : functionIdArray) {
				//弄持久态
				Function function = functionDAO.findOne(functionId);
				//改变一级缓存(角色关联权限)
				role.getFunctions().add(function);
				//等待快照更新。。。。
			}
		}
		
	}

}
