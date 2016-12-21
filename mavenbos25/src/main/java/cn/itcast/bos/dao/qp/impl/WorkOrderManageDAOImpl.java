package cn.itcast.bos.dao.qp.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.wltea.analyzer.lucene.IKAnalyzer;

import cn.itcast.bos.dao.qp.WorkOrderManageDAO;
import cn.itcast.bos.domain.qp.WorkOrderManage;

//dao的实现类
//实现类什么都不需要实现
//原理：spring data jpa会自动<jpa:repositories base-package="cn.itcast.bos.dao"/>
//优先寻找接口类名+Impl后缀的类，作为实现类，并自动继承simple。。。Repository
public class WorkOrderManageDAOImpl{
	//想在spring data jpa中获取EntityManager
	@PersistenceContext//自动注入
	private EntityManager entityManager;
	
	/**
	 * 通过luncence查询数据
	 * 说明：
	 * @return
	 * @author 传智.BoBo老师
	 * @time：2016年2月24日 下午5:27:17
	 */
	public Page<WorkOrderManage> searchByLucence(Pageable pageable, String conditionName,
			String conditionValue){
		//分析：将关键字和值交给lucence（Hibernatesearch的api），绑定数据库的表查询，
		//Hibernate search会“自动”先从全文搜索中找id，然后通过id查询数据库
		//-----lucence的query对象
		//---第一类搜索：一类长词分词模糊匹配
		//参数1：lucence版本
		//参数2：索引的字段的名字（查询的字段的名字）
		//参数3：分词器
		QueryParser queryParser = new QueryParser(Version.LUCENE_31, conditionName, new IKAnalyzer());
		//查询的关键字（解析器会自动分词后再查询）
		Query fenciQuery=null;
		try {
			fenciQuery = queryParser.parse(conditionValue);
		} catch (ParseException e) {
			throw new RuntimeException("不能解析分词这个关键字: " + conditionValue, e);
		}
		////---第二类搜索：短词直接匹配
		
		//参数：直接字段名和值
		Query duanciQuery=new WildcardQuery(new Term(conditionName, conditionValue));
		
		//将两个query结合起来---生成lunce的最终查询对象
		BooleanQuery query = new BooleanQuery();
		query.add(fenciQuery, Occur.SHOULD);//must:相当于and，SHOULD相当于or
		query.add(duanciQuery, Occur.SHOULD);
		
		//根据EntityManager来创建一个全文实体管理对象
		 FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		 
		 //++++返回的是全文检索的查询对象---------最终要要Hibernate search。。。。---
		 //参数1。lucence Query对象
		 //参数2：实体类,带有索引注解的实体类
		 FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, WorkOrderManage.class);
		//-----上述代码，Hibernate search结合了lucence和jpa的查询数据库的查询，查询内部，都hs封装
		 //---封装结果集page对象
		 int total = fullTextQuery.getResultSize();//列表的总的数量
		 //现在分页数据，是Hibernate search来分调用jpa
		 fullTextQuery.setFirstResult(pageable.getPageNumber()*pageable.getPageSize());//起始索引
		 fullTextQuery.setMaxResults(pageable.getPageSize());//每页最大记录数
		 List<WorkOrderManage> content = fullTextQuery.getResultList();//数据列表
		 
		 //结果
		 Page<WorkOrderManage> page = new PageImpl<WorkOrderManage>(content, pageable, total);
		
		return page;
	}

}
