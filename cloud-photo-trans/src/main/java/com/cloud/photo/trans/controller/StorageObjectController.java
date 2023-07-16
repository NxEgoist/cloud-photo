package com.cloud.photo.trans.controller;

import com.cloud.photo.api.common.util.RequestUtil;
import com.cloud.photo.trans.entity.StorageObject;
import com.cloud.photo.trans.service.IStorageObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 资源池文件存储信息 前端控制器
 * </p>
 *
 * @author egoist
 * @since 2023-07-13
 */
@RestController
@RequestMapping("/trans")
public class StorageObjectController {
    @Autowired
    IStorageObjectService storageObjectService;

    @RequestMapping("/getStorageObjectById")
    public StorageObject getStorageObjectById(HttpServletRequest request, HttpServletResponse response,
                                              @RequestParam("storageObjectId") String storageObjectId){
        String requestId = RequestUtil.getRequestId(request);
        RequestUtil.printQequestInfo(request);

        return storageObjectService.getById(storageObjectId);
    }
}
