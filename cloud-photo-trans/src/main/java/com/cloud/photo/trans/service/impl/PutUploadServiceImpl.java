package com.cloud.photo.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.photo.api.common.bo.FileUploadBo;
import com.cloud.photo.api.common.common.CommonEnum;
import com.cloud.photo.trans.entity.FileMd5;
import com.cloud.photo.trans.entity.StorageObject;
import com.cloud.photo.trans.service.IFileMd5Service;
import com.cloud.photo.trans.service.IPutUploadSerivce;
import com.cloud.photo.trans.service.IStorageObjectService;
import com.cloud.photo.trans.service.IUserFileService;
import com.cloud.photo.trans.utils.S3Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/*
用户id（String  userId）,文件大小(Long filSize),文件MD5（String fileMd5）,
文件名(String fileName)，资源池对象ID（String objectId）,资源池ID(String containerId)，
存储信息ID-秒传用(String storageObjectId)，文件分类(Integer category)
*/
@Service
public class PutUploadServiceImpl implements IPutUploadSerivce {

    @Autowired
    IStorageObjectService storageObjectService;
    @Autowired
    IUserFileService userFileService;
    @Autowired
    IFileMd5Service fileMd5Service;

//获取上传地址
    @Override
    public String getPutUploadUrl(String fileName, String fileMd5, Long fileSize) {
        FileMd5 fileMd5Entity=fileMd5Service.getOne(new QueryWrapper<FileMd5>().eq("md5",fileMd5));
        //文件存在，进行秒传
        if (fileMd5Entity!=null){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("storageObjectId",fileMd5Entity.getStorageObjectId());
            return jsonObject.toJSONString();
        }

        //文件不存在
        String suffixName="";
        if (StringUtils.isNotBlank(fileName)){
            suffixName=fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
        }
        return S3Util.getPutUploadUrl(suffixName,fileMd5);
    }
//  处理非秒传
    @Override
    public CommonEnum commit(FileUploadBo bo) {
        //获取文件资源池存储信息
        S3ObjectSummary s3ObjectSummary= S3Util.getObjectInfo(bo.getObjectId());
//            文件未上传
        if (s3ObjectSummary==null){
            return CommonEnum.FILE_NOT_UPLOADED;
        }
//            文件上传错误
        if (!bo.getFileSize().equals(s3ObjectSummary.getSize())
                || !org.apache.commons.lang.StringUtils.equalsIgnoreCase(s3ObjectSummary.getETag(),bo.getFileMd5())){
            return CommonEnum.FILE_UPLOADED_ERROR;
        }
//            文件上传成功 -文件存储信息入库
        StorageObject storageObject=new StorageObject("huaweiOBS",bo.getContainerId(),bo.getObjectId()
                ,bo.getFileMd5(),bo.getFileSize());
        storageObjectService.save(storageObject);

//            文件MD5入库,秒传用
        FileMd5 fileMd5bo =new FileMd5(bo.getFileMd5(),bo.getFileSize(),storageObject.getStorageObjectId());
        fileMd5Service.save(fileMd5bo);

//            文件入库 -用户文件列表 -发送到审核、图片kafka列表
        bo.setStorageObjectId(storageObject.getStorageObjectId());
        boolean result = userFileService.saveAndFileDeal(bo);
        if (result==true){
            return CommonEnum.SUCCESS;
        } return CommonEnum.FILE_UPLOADED_ERROR;

    }
//  处理秒传
    @Override
    public CommonEnum commitTransSecond(FileUploadBo bo) {
        //        校验存储ID是否正确
        StorageObject storageObject=storageObjectService.getById(bo.getStorageObjectId());
        if (storageObject==null){
            return CommonEnum.FILE_UPLOADED_ERROR;
        }
//            检查秒传文件大小
        if (!storageObject.getObjectSize().equals(bo.getFileSize())
                || !StringUtils.equalsIgnoreCase(bo.getFileMd5(),storageObject.getMd5())){
            return CommonEnum.FILE_UPLOADED_ERROR;
        }
//            保存文件入库 -用户文件列表  -发送到审核、图片kafka列表
        boolean result=userFileService.saveAndFileDeal(bo);

        if (result==true){
            return CommonEnum.SUCCESS;
        } return CommonEnum.FILE_UPLOADED_ERROR;
    }
}
