package com.taotao.manage.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {

    //private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private ItemCatService itemCatService;

    // @RequestMapping(method = RequestMethod.GET)
    // public ResponseEntity<String> queryAllItemCat(
    // @RequestParam(value = "callback", required = false) String callback) {
    // try {
    // ItemCatResult itemCatResult = this.itemCatService.queryAllItemCat();
    // String jsonResult = MAPPER.writeValueAsString(itemCatResult);
    // if (StringUtils.isNotEmpty(callback)) {
    // jsonResult = callback + "(" + jsonResult + ");";
    // }
    // return ResponseEntity.ok(jsonResult);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }

    /**
     * 对外提交接口服务，查询所有的商品类目
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryAllItemCat() {
        try {
            ItemCatResult itemCatResult = this.itemCatService.queryAllItemCat();
            return ResponseEntity.ok(itemCatResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
