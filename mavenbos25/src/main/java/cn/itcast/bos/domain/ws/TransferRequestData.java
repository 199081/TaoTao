package cn.itcast.bos.domain.ws;
//请求数据

import java.util.Map;

import com.google.gson.annotations.Expose;

//请求参数的bean的设计
public class TransferRequestData {
	@Expose
	private String opertype;
	@Expose
	private Map<String, String> param;
	
	public String getOpertype() {
		return opertype;
	}
	public void setOpertype(String opertype) {
		this.opertype = opertype;
	}
	public Map<String, String> getParam() {
		return param;
	}
	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	
	
}
