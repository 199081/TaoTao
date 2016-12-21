package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;

@RequestMapping("item/cache")
@Controller
public class ItemCacheController {

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "{itemId}", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteCache(@PathVariable("itemId") Long itemId) {
        try {
            // 删除Redis中的商品缓存数据
            String key = ItemService.REDIS_KEY + itemId;
            this.redisService.del(key);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
