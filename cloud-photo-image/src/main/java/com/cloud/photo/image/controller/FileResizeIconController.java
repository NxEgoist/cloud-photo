package com.cloud.photo.image.controller;

import com.cloud.photo.api.common.bo.FileResizeIconBo;
import com.cloud.photo.api.common.common.ResultBody;
import com.cloud.photo.api.common.util.RequestUtil;
import com.cloud.photo.image.service.IFileResizeIconService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 图片缩略图 前端控制器
 * </p>
 *
 * @author egoist
 * @since 2023-07-13
 */
@RestController
@RequestMapping("/image")
@Slf4j
public class FileResizeIconController {

    @Autowired
    IFileResizeIconService iFileResizeIconService;

    /**
     * 获取下载地址--通过资源池桶id，资源池objectid
     * @param request
     * @param response
     * @param fileResizeIconBo
     * @return
     */
    @RequestMapping("/getIconUrl")
    public ResultBody getIconUrl(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody FileResizeIconBo fileResizeIconBo){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        String url = iFileResizeIconService.getIconUrl(fileResizeIconBo.getUserId(),
                fileResizeIconBo.getFileId(), fileResizeIconBo.getIconCode());
        log.info("getPutUploadUrl() url = "+url );

        return ResultBody.success(url);
    }


}
