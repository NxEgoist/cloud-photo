package com.cloud.photo.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.photo.user.entity.User;
import com.cloud.photo.user.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Test
    public void insert() {
        User user = new User();
        user.setUserId("88888");
        user.setUserName("张三");
        userServiceImpl.save(user);
    }

    @Test
    public void getOne() {
        User user = userServiceImpl.getUserInfoById(5555L);

        System.out.println("================"+user.toString());
    }

    @Test
    public void getList(){
        QueryWrapper<User> qw = new QueryWrapper<User>();
        Page<User> page = new Page<User>(1, 2);
        IPage<User> userFileIPage = userServiceImpl.page(page,qw.orderByDesc("user_id","create_time"));
        List<User> users = userFileIPage.getRecords();
        for(User user : users){
            System.out.println(user);
        }
    }
}
