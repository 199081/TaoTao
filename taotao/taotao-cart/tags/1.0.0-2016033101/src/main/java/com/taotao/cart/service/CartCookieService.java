package com.taotao.cart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.util.CookieUtils;

@Service
public class CartCookieService {

    public static final String COOKIE_NAME = "TT_CART";

    public static final Integer COOKIE_TIME = 60 * 60 * 24 * 30 * 3;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ItemService itemService;

    /**
     * 查询商品列表, TODO: 按照创建时间倒序排序
     * 
     * @param request
     * @return
     */
    public List<Cart> queryCartList(HttpServletRequest request) {
        String cookieValue = CookieUtils.getCookieValue(request, COOKIE_NAME, true);
        if (StringUtils.isEmpty(cookieValue)) {
            return new ArrayList<Cart>(0);
        }
        try {
            return MAPPER.readValue(cookieValue,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Cart>(0);
    }

    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        // 判断该商品在购物车中是否存在，如果存在数量相加，不存在，直接添加
        List<Cart> carts = this.queryCartList(request);
        Cart cart = null;
        for (Cart c : carts) {
            if (c.getItemId().longValue() == itemId.longValue()) {
                cart = c;
            }
        }

        if (cart == null) {
            // 不存在
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setItemId(itemId);
            cart.setNum(1); // TODO 默认为1

            Item item = this.itemService.queryItemById(itemId);

            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImages()[0]);

            carts.add(cart);
        } else {
            // 存在
            cart.setNum(cart.getNum() + 1); // TODO 默认为1
            cart.setUpdated(new Date());
        }

        saveCartsToCookie(request, response, carts);
    }

    private void saveCartsToCookie(HttpServletRequest request, HttpServletResponse response, List<Cart> carts) {
        try {
            // 将购物车数据写入到cookie中
            CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(carts),
                    COOKIE_TIME, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void udpateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = this.queryCartList(request);
        for (Cart cart : carts) {
            if (cart.getItemId().longValue() == itemId.longValue()) {
                cart.setNum(num);
                cart.setUpdated(new Date());
                break;
            }
        }

        saveCartsToCookie(request, response, carts);
    }

    public void deleteItem(Long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<Cart> carts = this.queryCartList(request);
        for (Cart cart : carts) {
            if (cart.getItemId().longValue() == itemId.longValue()) {
                carts.remove(cart);
                break;
            }
        }

        saveCartsToCookie(request, response, carts);
    }
}
