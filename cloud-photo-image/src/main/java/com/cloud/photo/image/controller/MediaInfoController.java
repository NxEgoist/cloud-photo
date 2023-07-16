package com.cloud.photo.image.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.api.common.bo.FileAnalyzeBo;
import com.cloud.photo.api.common.bo.UserFileBo;
import com.cloud.photo.api.common.common.ResultBody;
import com.cloud.photo.api.common.feign.CloudPhotoTransService;
import com.cloud.photo.api.common.util.RequestUtil;
import com.cloud.photo.image.entity.MediaInfo;
import com.cloud.photo.image.service.IMediaInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件媒体信息 前端控制器
 * </p>
 *
 * @author egoist
 * @since 2023-07-13
 */
@RestController
@RequestMapping("/image/mediaInfo")
@Slf4j
public class MediaInfoController {

    @Autowired
    IMediaInfoService iMediaInfoService;

    @Autowired
    CloudPhotoTransService cloudPhotoTransService;

    /**
     * 获取下载地址--通过资源池桶id，资源池objectid
     * @param request
     * @param response
     * @param fileAnalyzeBo
     * @return
     */
    @RequestMapping("/getMediaInfo")
    public ResultBody getMediaInfo(HttpServletRequest request, HttpServletResponse response,
                                   @RequestBody FileAnalyzeBo fileAnalyzeBo){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        UserFileBo userFileBo = cloudPhotoTransService.getUserFileById(fileAnalyzeBo.getFileId());
        MediaInfo mediaInfo = iMediaInfoService.getOne(new QueryWrapper<MediaInfo>().eq("Storage_Object_Id", userFileBo.getStorageObjectId()),false);
        log.info("getMediaInfo() mediaInfo = "+mediaInfo );
        return ResultBody.success(mediaInfo);
    }
}
