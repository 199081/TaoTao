package com.taotao.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemParamItemService extends BaseService<ItemParamItem>{
    
    @Autowired
    private ItemParamItemMapper itemParamItemMapper;

    public Integer updateItemParamItemByItemId(Long itemId, String itemParams) {
        //更新的数据
        ItemParamItem record = new ItemParamItem();
        record.setParamData(itemParams);
        
        //更新的条件
        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId", itemId);
        
        return this.itemParamItemMapper.updateByExampleSelective(record, example);
    }

}
