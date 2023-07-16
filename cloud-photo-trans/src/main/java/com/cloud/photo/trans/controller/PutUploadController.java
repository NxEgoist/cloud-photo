package com.cloud.photo.trans.controller;

import com.cloud.photo.api.common.bo.FileUploadBo;
import com.cloud.photo.api.common.common.CommonEnum;
import com.cloud.photo.api.common.common.ResultBody;
import com.cloud.photo.api.common.util.RequestUtil;
import com.cloud.photo.trans.service.IPutUploadSerivce;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/trans")
@Slf4j
public class PutUploadController {

    @Autowired
    IPutUploadSerivce putUploadService;

    @RequestMapping("/getPutUploadUrl")
    public ResultBody getPutUploadUrl(HttpServletRequest request , HttpServletResponse response,
                                      @RequestParam(value = "userId",required = false) String userId,
                                      @RequestParam(value = "fileSize",required = false) Long fileSize,
                                      @RequestParam(value = "fileMd5",required=false) String fileMd5,
                                      @RequestParam(value = "fileName") String fileName){

        Long startTime =System.currentTimeMillis();
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);
        String result =putUploadService.getPutUploadUrl(fileName,fileMd5,fileSize);
        Long endTime=System.currentTimeMillis();
        log.info("getPutUploadUrl() userId="+userId+",result="+result+",cost="+(endTime-startTime)+"ms");
        return ResultBody.success(result,requestId);
    }

    @PostMapping("/commit")
    public ResultBody commit(HttpServletRequest request, HttpServletResponse response,
                             @RequestBody FileUploadBo bo){
        //打印请求日志
        String requestId =RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);

//        返回值
        CommonEnum result;
//      判断文件是否秒传
        if (StringUtils.isBlank(bo.getStorageObjectId())){
//            处理非秒传
            result=putUploadService.commit(bo);
        }else {
//            秒传
            result =putUploadService.commitTransSecond(bo);
        }
        log.info("getPutUploadUrl() userId="+bo.getUserId()+",result="+result);
        if (StringUtils.equals(result.getResultMsg(),CommonEnum.SUCCESS.getResultMsg())){
            return ResultBody.success(CommonEnum.SUCCESS.getResultMsg(),requestId);
        }else {
            return ResultBody.error(result.getResultCode(),result.getResultMsg(),requestId);
        }
    }


}
