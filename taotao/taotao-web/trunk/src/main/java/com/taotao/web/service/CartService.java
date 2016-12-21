package com.taotao.web.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.User;
import com.taotao.web.threadlocal.UserThreadLocal;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${TAOTAO_CART_URL}")
    private String TAOTAO_CART_URL;

    /**
     * 通过购物车系统提供的接口获取购物车商品列表
     * 
     * @return
     */
    public List<Cart> queryCartList() {
        User user = UserThreadLocal.get();
        String url = TAOTAO_CART_URL + "/service/api/cart/" + user.getId();
        try {
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            return MAPPER.readValue(jsonData,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
