package com.cloud.photo.user.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.api.common.bo.UserBo;
import com.cloud.photo.api.common.common.CommonEnum;
import com.cloud.photo.api.common.common.ResultBody;
import com.cloud.photo.api.common.constant.CommonConstant;
import com.cloud.photo.user.entity.User;
import com.cloud.photo.user.service.IUserService;
import com.cloud.photo.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author egoist
 * @since 2023-07-11
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    //http://localhost:9008/user/getUserInfo?phone=123121

    //ResponseBody: 将接口返回的数据给渲染成JSON串
//    @ResponseBody

    //GetMapping : 接收一个get请求的接口
    // http请求：get、post、delete、put、options
    //RequestParam：接收一个请求参数
    @GetMapping("/getUserInfo")
    public ResultBody getUserInfo(@RequestParam(value = "phone") String phone) {
        log.info("getUserInfo()-phone=" + phone + ",start!");
        //从user表里面获取一条记录，phone=传进来的phone
        //new QueryWrapper<User>().eq("phone", phone)
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        //打印拿到的信息
        log.info("getUserInfo()-phone=" + phone + ",user=" + user);

        //1、json形式的数据
        //2、自定义业务的结构体

        //ResultBody.error(CommonEnum.USER_IS_NULL)

        ResultBody resultBody = (user == null ? ResultBody.error(CommonEnum.USER_IS_NULL) : ResultBody.success(user));
        log.info("getUserInfo()-phone=" + phone + ",resultBody=" + resultBody);
        return resultBody;
    }

//    /**
//     * RequestBody: 接收一个实体：用户实体
//     * 新增一个用户
//     */
//    @PostMapping("/addUser")
//    public ResultBody addUser(@RequestBody User user){
//
//        //接收到用户实体,进行保存操作
//        boolean result = userService.save(user);
//
//        return result ? ResultBody.success() : ResultBody.error(CommonEnum.SAVE_ERROR);
//    }
    /**
     * 查询用户是否存在
     *
     * @param phone 手机号
     * @return 查询结果
     */
    @GetMapping("/checkPhone")
    public ResultBody checkPhone(@RequestParam(value = "phone") String phone) {
        User userEntity = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        return userEntity == null ? ResultBody.error(CommonEnum.USER_IS_NULL) : ResultBody.success();
    }

    /**
     * 检查账密
     * @param userName 账号
     * @param password 密码
     * @return 查询结果
     */
    @GetMapping("/checkAdmin")
    public ResultBody checkAdmin(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password){
        User admin = userService.getOne(new QueryWrapper<User>()
                .eq("user_name", userName)
                .eq("password", password)
                .eq("role", CommonConstant.ADMIN));
        return admin == null ? ResultBody.error(CommonEnum.USERNAME_PASSWORD_ERROR) : ResultBody.success();
    }

    /**
     * 执行用户登录
     *
     * @param bo 存放用户信息的实体
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResultBody login(@RequestBody UserBo bo) {
        String phone = bo.getPhone();
        //看看有没有这个用户
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        if (user == null) {
            //没有就新增这个用户信息
            user = new User();
            //复制
            BeanUtils.copyProperties(bo, user);
            //自定义一个用户ID
            user.setUserId(RandomUtil.randomString(9));
            user.setCreateTime(DateUtil.date());
            user.setUpdateTime(DateUtil.date());
            user.setLoginCount(0);
            //普通用户
            user.setRole("user");

        } else {
            //有这个用户就更新下登录信息
            user.setLoginCount(user.getLoginCount() + 1);
            user.setUpdateTime(DateUtil.date());
        }
        //更新信息入库
        boolean saveOrUpdate = userService.saveOrUpdate(user);
        return saveOrUpdate ? ResultBody.success() : ResultBody.error(CommonEnum.LOGIN_FAIL);
    }
//    @GetMapping("/id")
//    public ResultBody getUserInfo(@RequestParam(value = "userId") Long userId){
//
//        User user = userService.getUserInfoById(userId);
//        log.info("id:{},user{} ",userId,user);
//        return ResultBody.success(user);
//    }

}
