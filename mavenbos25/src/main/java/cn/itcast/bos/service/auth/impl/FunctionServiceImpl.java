package cn.itcast.bos.service.auth.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.auth.FucntionDAO;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.auth.FunctionService;
import cn.itcast.bos.service.base.BaseService;

//功能业务层实现类
@Service
@Transactional
public class FunctionServiceImpl extends BaseService implements FunctionService{
	//注入dao
	@Autowired
	private FucntionDAO functionDAO;

	@Override
	public List<Function> findFunctionList() {
		
//		return functionDAO.findAll();
		return functionDAO.findByFunctionIsNotNull();
	}

	@Override
	@CacheEvict(value="MenuSpringCache",allEntries=true)
	public void saveFunction(Function function) {
		//-----页面的下拉的值需要处理easyui-combobox,不选，会返回"";
		//不选意味着是一级菜单，根节点是0
		if(function.getFunction()!=null && StringUtils.isBlank(function.getFunction().getId()) ){
			function.getFunction().setId("0");
		}
		
		//----编号主键:手动生成
		//父id
		String pId =function.getFunction().getId();
		Function parentFunction = function.getFunction();
		
		
		//不管父id是多少，查询出父id下面的所有的子项，取所有子项id的最大值+1
		//调用dao，查询父节点的所有子节点的最大id值
		String maxId=functionDAO.findByFunciton(parentFunction);
		//当前id
		String id=null;
		if(StringUtils.isBlank(maxId)){
			//处理一级节点
			if(pId.equals("0")){
				id="101";
			}else{
				//还没有子节点
				id=pId+"001";
			}
			//一般的系统，对于一些基础数据，一般是要求有的，初始化数据。
			//所以根一般是有的
			
		}else{
			//返回值：一级节点104，二级101004
			//计算id
			//只需要加1
			//当前id
			id=String.valueOf(Integer.valueOf(maxId)+1);
		}
		
		//设置id
		function.setId(id);
		
		functionDAO.save(function);
	}

	@Override
//	@Cacheable(value="MenuSpringCache")
	@Cacheable(value="MenuSpringCache",key="#user.id")//你想根据用户来缓存对应的菜单
	//默认情况下，spring缓存就是一个map，将查询出来的对象的id作为map的key
	public List<Function> findFunctionByUser(User user) {
		if("admin".equals(user.getUsername())){
			//---如果是超管，要所有菜单
			return functionDAO.findMenuForAdmin();
		}else{
			//---如果是普通用户，根据权限来
			return functionDAO.findMenuForUser(user);
		}
	}

}
