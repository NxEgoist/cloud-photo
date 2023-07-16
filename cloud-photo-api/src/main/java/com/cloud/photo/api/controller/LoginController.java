package com.cloud.photo.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.cloud.photo.api.service.LoginService;
import com.cloud.photo.api.common.bo.UserBo;
import com.cloud.photo.api.common.common.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * api控制器
 *
 * @author 11446
 */
@RestController
@Slf4j
//@RequestMapping("/api")
public class LoginController {

//    @Autowired
//    private UserFeignService userFeignService;

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public ResultBody login(@RequestBody UserBo bo) {
        return loginService.login(bo);
    }

    @SaCheckLogin
    @PostMapping("/logout")
    public ResultBody logout() {
        return loginService.logout();
    }

    @SaCheckLogin
    @GetMapping("/getUserInfo")
    public ResultBody getUserInfo(){
        return loginService.getUserInfo();
    }

//    /**
//     * 获取用户信息
//     *
//     * @param phone 电话号码
//     * @return 用户信息
//     */
//    @GetMapping("/getUserInfo")
//    public ResultBody getUserInfo(@RequestParam("phone") String phone) {
//        log.info("getUserInfo() - phone=" + phone + ",start!");
//        //使用feign调用user服务
//        ResultBody resultBody = userFeignService.getUserInfo(phone);
//        log.info("getUserInfo() - phone=" + phone + ",resultBody" + resultBody);
//        return resultBody;
//    }


}
