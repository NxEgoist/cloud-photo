package com.cloud.photo.user.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author egoist
 * @since 2023-07-11
 */
@TableName("tb_user")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户Id
     */
    @TableId(type = IdType.INPUT)
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户所属地
     */
    private String department;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户创建时间
     */
    private Date createTime;

    /**
     * 用户最后登录时间
     */
    private Date updateTime;

    /**
     * 用户登录次数
     */
    private Integer loginCount;

    /**
     * 用户角色
     */
    private String role;

    /**
     * 用户出生年月日
     */
    private Date birth;

    private Integer newColumn;


}
