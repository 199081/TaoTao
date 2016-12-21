package cn.itcast.bos.web.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
//通用父action
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	//数据模型引用
	protected T model;
	@Override
	public T getModel() {
		return model;
	}
	
	//默认构造器初始化数据模型
	public BaseAction() {
		
		Class myClass =this.getClass();
		//向父类递归寻找泛型
		while(true){
			//得到带有泛型的类型，如BaseAction<Userinfo>
			Type type = myClass.getGenericSuperclass();
			
			if(type instanceof ParameterizedType){
				//转换为参数化类型
				ParameterizedType parameterizedType = (ParameterizedType) type;
				//获取泛型的第一个参数的类型类，如Userinfo
				Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
				//实例化模型对象
				try {
					model=modelClass.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}
			//寻找父类
			myClass=myClass.getSuperclass();
			
		}
		
	}
	
	/**
	 * 
	 * 说明：将对象放入session
	 * @param key
	 * @param value
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 下午4:14:44
	 */
	public void setObjectToSession(String key, Object value){
		ServletActionContext.getRequest().getSession().setAttribute(key, value);
	}
	
	/**
	 * 
	 * 说明：从参数中获取值
	 * @param key
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 下午4:46:06
	 */
	public String getValueFromParameter(String key){
		return ServletActionContext.getRequest().getParameter(key);
	}
	
	/**
	 * 
	 * 说明：从session中获取值
	 * @param key
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年1月30日 下午4:48:40
	 */
	public Object getValueFromSession(String key){
		return ServletActionContext.getRequest().getSession().getAttribute(key);
	}
	
	/**
	 * 常量：json字符串
	 */
	public static final String JSON="json";
	
	//属性驱动:自动封装参数到属性中
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}

}
