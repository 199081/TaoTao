package cn.itcast.bos.domain.ws;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

//实体类

public class Customer {
	
	@Expose
	private Integer id;//OID属性,使用的Oracle序列，
	@Expose
	private String name;//客户名称
	@Expose
	@SerializedName("address")
	private String residence;//住所
	@Expose
	private String telephone;//联系电话
	@Expose
	private String decidedZoneId;//定区编号（客户和定区关联的字段）//外键关联到bos系统的定区的主键
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResidence() {
		return residence;
	}
	public void setResidence(String residence) {
		this.residence = residence;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getDecidedZoneId() {
		return decidedZoneId;
	}
	public void setDecidedZoneId(String decidedZoneId) {
		this.decidedZoneId = decidedZoneId;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", residence=" + residence + ", telephone=" + telephone
				+ ", decidedZoneId=" + decidedZoneId + "]";
	}

	
}
