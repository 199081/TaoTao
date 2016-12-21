package cn.itcast.bos.service.auth;

import java.util.List;

import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.user.User;

//功能权限的service接口
public interface FunctionService {

	/**
	 * 
	 * 说明：查询功能列表（“所有”）
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月27日 上午9:48:32
	 */
	public List<Function> findFunctionList();

	/**
	 * 
	 * 说明：保存功能（包含菜单）
	 * @param function
	 * @author 传智.BoBo老师
	 * @time：2016年2月27日 上午10:41:02
	 */
	public void saveFunction(Function function);

	/**
	 * 
	 * 说明：根据用户查询功能菜单列表
	 * @param user
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月27日 下午4:23:24
	 */
	public List<Function> findFunctionByUser(User user);
	

}
