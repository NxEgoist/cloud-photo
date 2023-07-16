package com.cloud.photo.trans.controller;

import com.cloud.photo.api.common.common.ResultBody;
import com.cloud.photo.api.common.util.RequestUtil;
import com.cloud.photo.trans.service.IGetDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping("/trans")
public class getDownloadController {

    @Autowired
    IGetDownloadService getDownloadService;

    @RequestMapping("/getDownloadUrlByFileId")
    public ResultBody getDownloadUrlByFileId(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam String userId,@RequestParam String fileId){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        String url =getDownloadService.getDownloadUrlByFileId(userId,fileId);
        log.info("getPutUploadUrl() userId="+userId+",url="+url);
        return ResultBody.success(url);
    }
    /**
     * 获取下载地址--通过资源池桶id，资源池objectid
     * @param request
     * @param response
     * @param containerId
     * @param objectId
     * @return
     */
    @RequestMapping("/getDownloadUrl")
    public ResultBody getDownloadUrl(HttpServletRequest request, HttpServletResponse response,
                                     @RequestParam String containerId ,@RequestParam String objectId){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        String url = getDownloadService.getDownloadUrl(containerId,objectId);
        log.info("getPutUploadUrl() url = "+url );
        return ResultBody.success(url);
    }


}
