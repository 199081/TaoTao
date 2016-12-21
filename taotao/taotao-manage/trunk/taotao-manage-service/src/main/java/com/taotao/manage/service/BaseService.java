package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

public abstract class BaseService<T extends BasePojo> {
    
//    public abstract Mapper<T> getMapper();
    
    @Autowired
    private Mapper<T> mapper;
    
    /**
     * 根据id查询数据
     * 
     * @param id
     * @return
     */
    public T queryById(Long id){
        return this.mapper.selectByPrimaryKey(id);
    }
    
    /**
     * 查询所有数据
     * 
     * @return
     */
    public List<T> queryAll(){
        return this.mapper.select(null);
    }
    
    /**
     * 根据条件查询一条数据，如果有多条会抛出异常
     * 
     * @param record
     * @return
     */
    public T queryOne(T record){
        return this.mapper.selectOne(record);
    }
    
    /**
     * 根据条件查询数据列表
     * 
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record){
        return this.mapper.select(record);
    }
    
    /**
     * 分页查询数据列表
     * @param record 查询条件
     * @param page 页数
     * @param rows 页面大小
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record, Integer page, Integer rows){
        //设置分页参数
        PageHelper.startPage(page, rows);
        //执行查询
        List<T> list= this.mapper.select(record);
        return new PageInfo<T>(list);
    }
    
    /**
     * 新增数据，设置数据的创建时间和更新时间
     * @param t
     * @return
     */
    public Integer save(T t){
        t.setCreated(new Date());
        t.setUpdated(t.getCreated());
        return this.mapper.insert(t);
    }

    /**
     * 新增(有选择的，选择不为null的字段插入)数据，设置数据的创建时间和更新时间
     * @param t
     * @return
     */
    public Integer saveSelective(T t){
        t.setCreated(new Date());
        t.setUpdated(t.getCreated());
        return this.mapper.insertSelective(t);
    }
    
    /**
     * 更新数据，设置数据的更新时间
     * @param t
     * @return
     */
    public Integer update(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(t);
    }

    /**
     * 更新(有选择的，选择不为null的字段更新)数据，设置数据的更新时间
     * 
     * @param t
     * @return
     */
    public Integer updateSelective(T t){
        t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(t);
    }
    
    /**
     * 根据id删除数据
     * 
     * @param id
     * @return
     */
    public Integer deleteById(Long id){
        return this.mapper.deleteByPrimaryKey(id);
    }
    
    /**
     * 批量删除数据
     * 
     * @param clazz
     * @param property
     * @param values
     * @return
     */
    public Integer deleteByIds(Class<T> clazz, String property, List<Object> values){
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, values);
        return this.mapper.deleteByExample(example);
    }
    
    /**
     * 根据条件删除数据
     * 
     * @param record
     * @return
     */
    public Integer deleteByWhere(T record){
        return this.mapper.delete(record);
    }
}
