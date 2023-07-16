package com.cloud.photo.trans.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.photo.api.common.bo.AlbumPageBo;
import com.cloud.photo.api.common.common.ResultBody;
import com.cloud.photo.api.common.util.RequestUtil;
import com.cloud.photo.trans.entity.UserFile;
import com.cloud.photo.trans.service.IUserFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author egoist
 * @since 2023-07-13
 */
@RestController
@RequestMapping("/trans")
public class UserFileController {
    @Autowired
    IUserFileService userFileService;

    //获取文件列表
    @PostMapping("/userFileList")
    public ResultBody userFileList(HttpServletRequest request, HttpServletResponse response, @RequestBody AlbumPageBo pageBo){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request,pageBo);

        //1.设置查询Mapper
        QueryWrapper<UserFile> qw=new QueryWrapper<>();
        //2.组装查询条件
        HashMap<String,Object> param =new HashMap<>();
        if (pageBo.getCategory()!=null){
            param.put("category",pageBo.getCategory());
        }
        param.put("user_id",pageBo.getUserId());
        qw.allEq(param);
        //3.分页
        Integer current=pageBo.getCurrent();
        Integer pageSize=pageBo.getPageSize();
        if (current ==null){
            current=1;
        }
        if (pageSize==null){
            pageSize=20;
        }
        //4.查询列表数据
        Page<UserFile> page=new Page<>(current,pageSize);
        IPage<UserFile> userFileIPage=userFileService.page(page,qw.orderByDesc("user_id","create_time"));
        return ResultBody.success(userFileIPage);
    }

    //获取文件信息
    @RequestMapping("/getUserFileById")
    public UserFile getUserFileById(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam("fileId") String fileId){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);

        return userFileService.getById(fileId);
    }

    //更新文件信息
    @PostMapping("/updateUserFile")
    public Boolean updateUserFile(HttpServletRequest request, HttpServletResponse response,
                                  @RequestBody List<UserFile> userFileBoList){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request,userFileBoList);
        for(UserFile userFile:userFileBoList){
            UpdateWrapper<UserFile> updateWrapper = new UpdateWrapper<UserFile>();

            //通过存储id更新审核状态
            if(StrUtil.isNotBlank(userFile.getStorageObjectId())){
                updateWrapper.eq("storage_object_id",userFile.getStorageObjectId());
            }
            //通过文件id更新审核状态
            if(StrUtil.isNotBlank(userFile.getUserFileId())){
                updateWrapper.eq("user_file_id",userFile.getUserFileId());
            }
            updateWrapper.set("audit_status",userFile.getAuditStatus());
            userFileService.update(updateWrapper);
        }
        return true;
    }
}
