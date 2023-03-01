package com.lmzz.threadlocal.userDemo;

import com.lmzz.threadlocal.util.MyThreadLocal;
import com.lmzz.threadlocal.util.ThreadLocalUtil2;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    public void testMethod(){
        System.out.println("UserServiceImpl。。。。。。");
        User usr = (User) MyThreadLocal.get();
        System.out.println(usr);

        Object user = ThreadLocalUtil2.get("USER");
        System.out.println(user);

    }
}
