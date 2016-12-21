package com.taotao.sso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.util.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;

@RequestMapping("user")
@Controller
public class UserController {

    private static final String COOKIE_NAME = "TT_TOKEN";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {
        return "register";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {
        return "login";
    }

    /**
     * 检测数据是否可用
     * 
     * @param param
     * @param type
     * @return
     */
    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkParam(@PathVariable("param") String param,
            @PathVariable("type") Integer type) {
        try {
            Boolean bool = this.userService.checkParam(param, type);
            if (null == bool) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            // 为了兼容前端JS逻辑，做出妥协（已经联系了前端团队，但是前端团队不做修改，故此做出无奈之举.....）
            return ResponseEntity.ok(!bool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 实现用户注册
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doRegister(@Valid User user, BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (bindingResult.hasErrors()) {
            // 校验中有不符合规则的内容
            List<String> msgs = new ArrayList<String>();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                msgs.add(objectError.getDefaultMessage());
            }
            result.put("status", "400");
            result.put("data", StringUtils.join(msgs, '|'));
            return result;
        }

        Boolean bool = this.userService.doRegister(user);
        if (bool) {
            result.put("status", "200");
        } else {
            result.put("status", "250");
            result.put("data", "好的，我知道啦!");
        }
        return result;
    }

    /**
     * 登录
     * 
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> doLogin(@RequestParam("username") String username,
            @RequestParam("password") String password, HttpServletRequest request,
            HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String token = this.userService.doLogin(username, password);
            if (StringUtils.isEmpty(token)) {
                // 登录失败
                result.put("status", 250);
            } else {
                // 登录成功
                result.put("status", 200);

                // 将token写入到cookie中
                CookieUtils.setCookie(request, response, COOKIE_NAME, token);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
        }
        return result;
    }

    /**
     * 根据token查询用户数据
     * 
     * @param token
     * @return
     */
    @RequestMapping(value = "{token}", method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token") String token) {
        try {
            User user = this.userService.queryUserByToken(token);
            if(null == user){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
