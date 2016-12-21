package com.taotao.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer REDIS_TIME = 60 * 30;

    public Boolean checkParam(String param, Integer type) {
        User record = new User();

        switch (type) {
        case 1:
            record.setUsername(param);
            break;
        case 2:
            record.setPhone(param);
            break;
        case 3:
            record.setEmail(param);
            break;
        default:
            return null;
        }

        return this.userMapper.selectOne(record) == null;
    }

    public Boolean doRegister(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(user.getCreated());

        // 加密密码，通过MD5加密实现
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        return this.userMapper.insert(user) == 1;
    }

    public String doLogin(String username, String password) throws Exception {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if (null == user) {
            // 用户名不存在
            return null;
        }
        // 对比密码是否相同
        if (!StringUtils.equals(DigestUtils.md5Hex(password), user.getPassword())) {
            return null;
        }

        // 登录成功
        String token = DigestUtils.md5Hex(System.currentTimeMillis() + username);
        this.redisService.set("TOKEN_" + token, MAPPER.writeValueAsString(user), REDIS_TIME);
        return token;
    }

    public User queryUserByToken(String token) {
        String key = "TOKEN_" + token;
        String data = this.redisService.get(key);
        if (StringUtils.isEmpty(data)) {
            // 登录超时
            return null;
        }
        try {
            // 重新设置Redis中生存时间
            this.redisService.expire(key, REDIS_TIME);
            
            return MAPPER.readValue(data, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
