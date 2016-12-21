package com.taotao.web.threadlocal;

import com.taotao.cart.bean.User;

public class UserThreadLocal {
    
    private static ThreadLocal<User> local = new ThreadLocal<User>();
    
    private UserThreadLocal(){
        
    }
    
    public static void set(User user){
        local.set(user);
    }

    public static User get(){
        return local.get();
    }
}
