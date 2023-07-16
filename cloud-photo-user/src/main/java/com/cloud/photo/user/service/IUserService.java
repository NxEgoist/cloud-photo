package com.cloud.photo.user.service;

import com.cloud.photo.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author egoist
 * @since 2023-07-11
 */
public interface IUserService extends IService<User> {

    User getUserInfoById(Long userId);
}
