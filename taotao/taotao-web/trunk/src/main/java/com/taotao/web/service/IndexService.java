package com.taotao.web.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Content;

@Service
public class IndexService {

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    @Value("${INDEX_AD1_URL}")
    private String INDEX_AD1_URL;
    
    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ApiService apiService;

    public String queryIndexAD1() {
        try {
            // 通过后台系统的接口获取数据
            String url = TAOTAO_MANAGE_URL + INDEX_AD1_URL;
            String jsonData = this.apiService.doGet(url);
            // 解析json数据，封装成前台所需要的数据结构
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");

            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

            for (JsonNode row : rows) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("srcB", row.get("pic").asText());
                map.put("height", 240);
                map.put("alt", row.get("title").asText());
                map.put("width", 670);
                map.put("src", row.get("pic").asText());
                map.put("widthB", 550);
                map.put("href", row.get("url").asText());
                map.put("heightB", 240);

                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String queryIndexAD2() {
        try {
            // 通过后台系统的接口获取数据
            String url = TAOTAO_MANAGE_URL + INDEX_AD2_URL;
            String jsonData = this.apiService.doGet(url);
            
            EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Content.class);
            @SuppressWarnings("unchecked")
            List<Content> contents = (List<Content>) easyUIResult.getRows();

            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            
            for (Content content : contents) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("width", 310);
                map.put("height", 70);
                map.put("src", content.getPic());
                map.put("href", content.getUrl());
                map.put("alt",content.getTitle());
                map.put("widthB", 210);
                map.put("heightB", 70);
                map.put("srcB", content.getPic());
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
//    public String queryIndexAD2() {
//        try {
//            // 通过后台系统的接口获取数据
//            String url = TAOTAO_MANAGE_URL + INDEX_AD2_URL;
//            String jsonData = this.apiService.doGet(url);
//            // 解析json数据，封装成前台所需要的数据结构
//            JsonNode jsonNode = MAPPER.readTree(jsonData);
//            ArrayNode rows = (ArrayNode) jsonNode.get("rows");
//
//            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
//            
//            for (JsonNode row : rows) {
//                Map<String, Object> map = new LinkedHashMap<String, Object>();
//                map.put("width", 310);
//                map.put("height", 70);
//                map.put("src", row.get("pic").asText());
//                map.put("href", row.get("url").asText());
//                map.put("alt", row.get("title").asText());
//                map.put("widthB", 210);
//                map.put("heightB", 70);
//                map.put("srcB", row.get("pic").asText());
//                result.add(map);
//            }
//            return MAPPER.writeValueAsString(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
