package com.cloud.photo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.user.entity.User;
import com.cloud.photo.user.mapper.UserMapper;
import com.cloud.photo.user.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author egoist
 * @since 2023-07-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    public  User getUserInfoById(Long userId){

        QueryWrapper queryWrapper =  new QueryWrapper<User>();
        queryWrapper.eq("USER_ID",userId);
        return  this.getOne(queryWrapper);

    }

}
