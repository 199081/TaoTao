package com.taotao.search.mq.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.search.bean.Item;
import com.taotao.search.service.ItemService;

public class ItemMQHandler {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private HttpSolrServer httpSolrServer;
    
    public void execute(String msg){
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            String type = jsonNode.get("type").asText();
            if(StringUtils.equals("insert", type) || StringUtils.equals("update", type)){
                // 通过后台系统的接口获取商品数据
                Item item = this.itemService.queryItemById(itemId);
                
                // 提交数据到Solr
                this.httpSolrServer.addBean(item);
                this.httpSolrServer.commit();
            }else if(StringUtils.equals("delete", type)){
                //将Solr中索引数据删除
                this.httpSolrServer.deleteById(String.valueOf(itemId));
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
