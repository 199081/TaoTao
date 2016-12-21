package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Order;

@Service
public class OrderService {

    @Autowired
    private ApiService apiService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    @Value("${TAOTAO_ORDER_URL}")
    private String TAOTAO_ORDER_URL;

    /**
     * 需要調用订单系統的接口提交数据
     * 
     * @param order
     * @return
     */
    public String submitOrder(Order order) {
        String url = TAOTAO_ORDER_URL + "/order/create";
        try {
          HttpResult httpResult =  this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
          if(httpResult.getStatusCode().intValue() == 200){
              JsonNode jsonNode = MAPPER.readTree(httpResult.getData());
              if(jsonNode.get("status").intValue() == 200){
                  //提交成功
                  return jsonNode.get("data").asText();
              }
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据订单id查询订单数据
     * 
     * @param orderId
     * @return
     */
    public Order queryById(String orderId) {
        String url = TAOTAO_ORDER_URL + "/order/query/" + orderId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isNotEmpty(jsonData)){
                return MAPPER.readValue(jsonData, Order.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
