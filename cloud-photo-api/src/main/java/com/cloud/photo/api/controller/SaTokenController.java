package com.cloud.photo.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.cloud.photo.api.common.common.CommonEnum;
import com.cloud.photo.api.common.common.ResultBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaTokenController {
    @GetMapping("/saTokenLogin")
    public ResultBody saTokenLogin(@RequestParam(value = "phone") String phone){
        StpUtil.login(phone);

        boolean login=StpUtil.isLogin();
        if (login){
            String tokenValue = StpUtil.getTokenInfo().getTokenValue();
            System.out.println(tokenValue);
            return ResultBody.success();
        }else return ResultBody.error(CommonEnum.LOGIN_FAIL);

    }

    //@SaCheckLogin  //校验用户登入

}
