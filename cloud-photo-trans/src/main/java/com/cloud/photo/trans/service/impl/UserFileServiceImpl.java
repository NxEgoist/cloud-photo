package com.cloud.photo.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.photo.api.common.bo.FileUploadBo;
import com.cloud.photo.api.common.constant.CommonConstant;
import com.cloud.photo.trans.entity.StorageObject;
import com.cloud.photo.trans.entity.UserFile;
import com.cloud.photo.trans.mapper.UserFileMapper;
import com.cloud.photo.trans.service.IStorageObjectService;
import com.cloud.photo.trans.service.IUserFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author egoist
 * @since 2023-07-13
 */
@Service
public class UserFileServiceImpl extends ServiceImpl<UserFileMapper, UserFile> implements IUserFileService {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @Autowired
    private IStorageObjectService storageObjectService;

    @Override
    public boolean saveAndFileDeal(FileUploadBo bo) {

        UserFile userFile=new UserFile();

        userFile.setUserId(bo.getUserId());
        userFile.setFileStatus(CommonConstant.FILE_STATUS_NORMA); //file normal
        userFile.setCreateTime(LocalDateTime.now());
        userFile.setFileName(bo.getFileName());
        userFile.setIsFolder(CommonConstant.FILE_IS_FOLDER_NO);
        userFile.setAuditStatus(CommonConstant.FILE_AUDIT_ACCESS);
        userFile.setFileSize(bo.getFileSize());
        userFile.setModifyTime(LocalDateTime.now());
        userFile.setStorageObjectId(bo.getStorageObjectId());
        userFile.setCategory(bo.getCategory());

        String storageObjectId=userFile.getStorageObjectId();
        StorageObject storageObject = storageObjectService.getById(storageObjectId);
        userFile.setFileMd5(storageObject.getMd5());


        Boolean result =this.save(userFile);

        //图片处理-格式分析
        kafkaTemplate.send(CommonConstant.FILE_AUDIT_TOPIC, JSONObject.toJSONString(userFile));
        //文件审核处理
        kafkaTemplate.send(CommonConstant.FILE_IMAGE_TOPIC,JSONObject.toJSONString(userFile));

        return result;
    }
}
