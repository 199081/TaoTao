package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 根据父节点id查询列表
     * 
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryListByParentId(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {
        try {
            ContentCategory record = new ContentCategory();
            record.setParentId(parentId);
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if (null == list || list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 新增节点
     * 
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {
        try {
            // 设置初始值
            contentCategory.setId(null);
            contentCategory.setIsParent(false);
            contentCategory.setSortOrder(1);
            contentCategory.setStatus(1);
            this.contentCategoryService.save(contentCategory);

            // 判断该节点的父节点的isParent是否为true，如果为false，设置为true
            ContentCategory parent = this.contentCategoryService.queryById(contentCategory.getParentId());
            if (!parent.getIsParent()) {
                parent.setIsParent(true);
                this.contentCategoryService.update(parent);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 重命名
     * 
     * @param id
     * @param name
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> rename(@RequestParam("id") Long id, @RequestParam("name") String name) {
        try {
            ContentCategory contentCategory = new ContentCategory();
            contentCategory.setId(id);
            contentCategory.setName(name);
            this.contentCategoryService.updateSelective(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 删除节点包含它的所有子节点
     * 
     * @param id
     * @param parentId
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam("id") Long id, @RequestParam("parentId") Long parentId) {
        try {
            // 定义集合，用于收集待删除的id
            List<Object> ids = new ArrayList<Object>();
            ids.add(id);

            // 查找该节点下的所有子节点
            findAllSubNodes(id, ids);

            this.contentCategoryService.deleteByIds(ContentCategory.class, "id", ids);

            // 判断该节点是否还有其他兄弟节点，如果没有，设置父节点的isParent为false
            ContentCategory record = new ContentCategory();
            record.setParentId(parentId);
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if (null == list || list.isEmpty()) {
                // 没有兄弟节点
                ContentCategory parent = new ContentCategory();
                parent.setId(parentId);
                parent.setIsParent(false);
                this.contentCategoryService.updateSelective(parent);
            }

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private void findAllSubNodes(Long id, List<Object> ids) {
        ContentCategory record = new ContentCategory();
        record.setParentId(id);
        List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            if (contentCategory.getIsParent()) {
                // 开始递归
                findAllSubNodes(contentCategory.getId(), ids);
            }
        }
    }

}
