package com.taotao.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private ItemMapper itemMapper;

    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;

    @Autowired
    private ApiService apiService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Boolean saveItem(Item item, String desc, String itemParams) {
        // 初始化数据
        item.setStatus(1);
        // 强制设置为null，避免id被前端参数注入
        item.setId(null);

        // 保存商品的基本数据
        Integer count1 = super.save(item);

        // 保存商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = this.itemDescService.save(itemDesc);

        // 保存商品规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        Integer count3 = this.itemParamItemService.save(itemParamItem);
        
        // 发送消息，通知其他系统
        sendMsg(item.getId(), "insert");

        return count1.intValue() == 1 && count2.intValue() == 1 && count3.intValue() == 1;
    }

    public EasyUIResult queryItemList(Integer page, Integer rows) {
        // 设置分页参数
        PageHelper.startPage(page, rows);

        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");
        List<Item> list = this.itemMapper.selectByExample(example);

        PageInfo<Item> pageInfo = new PageInfo<Item>(list);

        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    public Boolean updateItem(Item item, String desc, String itemParams) {
        // 强制设置不能更新的字段为null
        item.setCreated(null);
        item.setStatus(null);

        Integer count1 = super.updateSelective(item);

        // 更新商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        Integer count2 = this.itemDescService.updateSelective(itemDesc);

        Integer count3 = this.itemParamItemService.updateItemParamItemByItemId(item.getId(), itemParams);
        
        // 发送消息，通知其他系统
        sendMsg(item.getId(), "update");

//        try {
//            // 通知其它系统该商品数据已经更新
//            String url = TAOTAO_WEB_URL + "/item/cache/" + item.getId() + ".html";
//            this.apiService.doPost(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // TODO 发邮件、短信通知前台系统的相关人员检查接口
//            // 发邮件：
//            // 1、自己搭建邮箱服务器， 2、使用已有的邮箱服务器 （163，qq，sina等）
//            // 发送：Apache的commons-email来发送
//            
//            // 发短信(106平台)：
//            // 1、自己调用运营商的接口发送， 2、通过第三方平台发送(推荐) 
//            // 课前资料中有一个第三方短信平台的文档，每注册一个账户赠送10条短信
//        }

        return count1.intValue() == 1 && count2.intValue() == 1 && count3.intValue() == 1;
    }
    
    private void sendMsg(Long itemId, String type){
        try {
            Map<String, Object> msg = new HashMap<String, Object>();
            msg.put("itemId", itemId);
            msg.put("type", type);
            msg.put("date", System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
