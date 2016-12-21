package com.taotao.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.cart.bean.Item;
import com.taotao.cart.bean.User;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.web.threadlocal.UserThreadLocal;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemService itemService;

    public void addItemToCart(Long itemId) {
        // 判断该商品在购物车中是否存在，如果存在数量相加，不存在，直接添加
        User user = UserThreadLocal.get();
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(user.getId());
        Cart cart = this.cartMapper.selectOne(record);
        if (null == cart) {
            // 不存在
            cart = new Cart();
            cart.setCreated(new Date());
            cart.setUpdated(cart.getCreated());
            cart.setItemId(itemId);
            cart.setUserId(user.getId());
            cart.setNum(1); // TODO 默认为1

            Item item = this.itemService.queryItemById(itemId);

            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImages()[0]);

            this.cartMapper.insert(cart);
        } else {
            // 存在
            cart.setNum(cart.getNum() + 1); // TODO 数量默认为1
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKeySelective(cart);
        }
    }

    public List<Cart> queryCartList() {
        return this.queryCartList(UserThreadLocal.get().getId());
    }

    public List<Cart> queryCartList(Long userId) {
        Example example = new Example(Cart.class);
        example.setOrderByClause("created DESC");
        example.createCriteria().andEqualTo("userId", userId);
        return this.cartMapper.selectByExample(example);
    }

    public void udpateNum(Long itemId, Integer num) {

        // 更新的数据
        Cart record = new Cart();
        record.setNum(num);
        record.setUpdated(new Date());

        // 更新条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId)
                .andEqualTo("userId", UserThreadLocal.get().getId());

        this.cartMapper.updateByExampleSelective(record, example);
    }

    public void deleteItem(Long itemId) {
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(UserThreadLocal.get().getId());
        this.cartMapper.delete(record);
    }

}
