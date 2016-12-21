package cn.itcast.bos.domain.bc;
// Generated 2016-2-18 10:53:03 by Hibernate Tools 3.2.2.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.GenericGenerator;

/**
 * Region generated by hbm2java
 */
@Entity
@Table(name="T_BC_REGION"
    ,schema="BOS25"
)
public class Region  implements java.io.Serializable {


     private String id;
     private String province;
     private String city;
     private String district;
     private String postcode;
     private String shortcode;
     private String citycode;
     private Set<Subarea> subareas = new HashSet<Subarea>(0);
     
     //添加一个getter属性，用于json显示key
     @Transient//不会将该属性作为数据库的字段
     public String getName(){
    	 return province+"-"+city+"-"+district;
     }

    public Region() {
    }

    public Region(String province, String city, String district, String postcode, String shortcode, String citycode, Set<Subarea> subareas) {
       this.province = province;
       this.city = city;
       this.district = district;
       this.postcode = postcode;
       this.shortcode = shortcode;
       this.citycode = citycode;
       this.subareas = subareas;
    }
   
     @Id 
    @Column(name="ID", unique=true, nullable=false, length=32)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="PROVINCE", length=50)
    public String getProvince() {
        return this.province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    @Column(name="CITY", length=50)
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    @Column(name="DISTRICT", length=50)
    public String getDistrict() {
        return this.district;
    }
    
    public void setDistrict(String district) {
        this.district = district;
    }
    
    @Column(name="POSTCODE", length=50)
    public String getPostcode() {
        return this.postcode;
    }
    
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    
    @Column(name="SHORTCODE", length=30)
    public String getShortcode() {
        return this.shortcode;
    }
    
    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }
    
    @Column(name="CITYCODE", length=30)
    public String getCitycode() {
        return this.citycode;
    }
    
    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="region")
@JSON(serialize=false)
    public Set<Subarea> getSubareas() {
        return this.subareas;
    }
    
    public void setSubareas(Set<Subarea> subareas) {
        this.subareas = subareas;
    }




}


