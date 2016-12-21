package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    /**
     * 新增商品
     * 
     * @param item
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("新增商品，item = {}, desc = {}", item, desc);
        }
        // 校验，待完善
        if (item.getPrice() == null || item.getPrice().intValue() == 0) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("新增商品时用户输入的参数不合法，item = {}, desc = {}", item, desc);
            }
            // 参数不合法
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            // 保存商品基本数据
            Boolean bool = this.itemService.saveItem(item, desc, itemParams);
            if (bool) {
                // 新增成功
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("新增商品成功! id = {}", item.getId());
                }
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        } catch (Exception e) {
            LOGGER.error("新增商品失败! item = " + item + ", desc = " + desc, e);
            e.printStackTrace();
        }
        // 新增失败
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 查询商品列表
     * 
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        try {
            EasyUIResult easyUIResult = this.itemService.queryItemList(page, rows);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 更新商品数据
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {
        try {
            Boolean bool = this.itemService.updateItem(item, desc, itemParams);
            if (bool) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 根据商品id查询商品基本数据
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Item> queryById(@PathVariable("itemId")Long itemId) {
        try {
            Item item = this.itemService.queryById(itemId);
            if(null == item){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
