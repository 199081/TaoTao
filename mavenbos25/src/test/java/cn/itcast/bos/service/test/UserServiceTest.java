package cn.itcast.bos.service.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.domain.user.User;
import cn.itcast.bos.service.UserService;
import cn.itcast.bos.utils.MD5Utils;

//
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class UserServiceTest {
	//注入测试beAN
	@Autowired
	private UserService userService;

	@Test
	public void testSaveUser() {
		//瞬时态对象
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");//密码一般加密
		//保存
		userService.saveUser(user);
	}

	@Test
	public void testFindAllUser() {
		List<User> list = userService.findAllUser();
		System.out.println(list);
	}
	
	@Test
	public void testPropertyExpressions(){
		//根据用户名查询用户
		User user = userService.findUserByUsername("admin");
		System.out.println(user);
		
		//根据用户名和密码查询用户列表
//		List<User> list = userService.findUserListByUsernameAndPassword("admin1", MD5Utils.md5("admin1") );
//		System.out.println(list);
		
		//登录
		User user2 = userService.login("admin", "admin");
		System.out.println(user2);
	}
	
	@Test
	public void testFindPasswordByUsername(){
		String password = userService.findPasswordByUsername("admin");
		System.out.println(password);
	}

}
