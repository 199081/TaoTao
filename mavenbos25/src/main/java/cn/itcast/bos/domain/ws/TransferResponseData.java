package cn.itcast.bos.domain.ws;
//结果的封装

import java.util.List;

import com.google.gson.annotations.Expose;

public class TransferResponseData<T> {
	@Expose
	private String status;
	@Expose
	private List<T> data;//封装任何的bean的列表
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	
	

}
