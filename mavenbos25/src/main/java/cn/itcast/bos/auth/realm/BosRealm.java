package cn.itcast.bos.auth.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.itcast.bos.dao.UserDAO;
import cn.itcast.bos.domain.auth.Function;
import cn.itcast.bos.domain.auth.Role;
import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.UserService;
import cn.itcast.bos.service.auth.RoleService;

//自定义的realm，作用从数据库查询数据，并返回数据库认证的信息
@Component("bosRealm")
public class BosRealm extends AuthorizingRealm{
	//注入ehcache的缓存区域
	@Value("BosShiroCache")//值在ehcache.xml中配置的一块区域
	public void setSuperAuthenticationCacheName(String authenticationCacheName){
		super.setAuthenticationCacheName(authenticationCacheName);
	}
	
	//注入用户的DAO
	@Autowired
	private UserService userService;
	//注入角色service
	@Autowired
	private RoleService roleService;

	@Override
	//授权:回调方法
	//如果返回null，说明没有权限，shiro会自动跳到<property name="unauthorizedUrl" value="/unauthorized.jsp" />
	//如果返回null，根据配置/page_base_subarea.action = roles["weihu"]，去自动匹配
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("开始授权了。。。。。");
		//要返回的对象
		SimpleAuthorizationInfo authorizationInfo=null;
		//获取当前登录的用户信息
		User user =(User)principals.getPrimaryPrincipal();
		//查询当前用户拥有的角色:调用serivce层
		List<Role> roleList = roleService.findRoleListByUser(user);
		if(!roleList.isEmpty()){
			//最终要授权信息对象
			authorizationInfo = new SimpleAuthorizationInfo(); 
			for (Role role : roleList) {
				//添加当前用户所拥有的角色
				authorizationInfo.addRole(role.getCode());
				//获取当前角色所拥有的权限(导航查询)
				Set<Function> functionSet = role.getFunctions();
				if(!functionSet.isEmpty()){
					for (Function function : functionSet) {
						//添加当前用户拥有的权限
						authorizationInfo.addStringPermission(function.getCode());
					}
				}
			}
		}
		
		return authorizationInfo;
	}

	@Override
	//认证:回调，认证管理器会将认证令牌放到这里（action层的令牌AuthenticationToken）
	//发现如果返回null，抛出用户不存在的异常UnknownAccountException
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("开始认证了。。。。。");
		//用户名密码令牌（acton传过来）
		UsernamePasswordToken upToken = (UsernamePasswordToken)token;
		//调用业务层来查询(根据用户名来查询用户，无需密码)
		User user = userService.findUserByUsername(upToken.getUsername());
		//判断用户是否存在
		if(null==user){
			//用户名不存在
			return null;//会自动抛出异常
		}else{
			//用户存在
			//参数1：用户对象，将来要放入session,数据库查询出来的用户
			//参数2：密码：密码校验：校验的动作交给shiro
			//参数3:你用哪个realm(bean的名字，自动寻找)
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), super.getName()); 
			return authenticationInfo;//密码校验失败，会自动抛出IncorrectCredentialsException
		}
		
	}

}
