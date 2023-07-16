package com.cloud.photo.trans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.photo.trans.entity.StorageObject;
import com.cloud.photo.trans.entity.UserFile;
import com.cloud.photo.trans.service.IGetDownloadService;
import com.cloud.photo.trans.service.IStorageObjectService;
import com.cloud.photo.trans.service.IUserFileService;
import com.cloud.photo.trans.utils.S3Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class GetDownloadServiceImpl implements IGetDownloadService {

    @Autowired
    IUserFileService userFileService;
    @Autowired
    IStorageObjectService storageObjectService;
    @Override
    public String getDownloadUrlByFileId(String userId, String fileId) {

//        查询文件信息
        UserFile userFile =userFileService.getById(fileId);
        if (userFile==null){
            log.error("getDownloadUrlByFileId() userFile is null,fileId="+fileId);
            return null;
        }
//        查询文件存储信息
        StorageObject storageObject=storageObjectService.getById(userFile.getStorageObjectId());
        if (storageObject==null){
            log.error("getDownloadUrlByFileId() storageObject is null, fileId="+fileId);
            return null;
        }
        //生成下载地址
        return S3Util.getDownloadUrl(storageObject.getContainerId(),storageObject.getObjectId(),userFile.getFileName());

    }

    @Override
    public String getDownloadUrl(String containerId, String objectId) {
        return S3Util.getDownloadUrl(containerId,objectId);
    }
}
