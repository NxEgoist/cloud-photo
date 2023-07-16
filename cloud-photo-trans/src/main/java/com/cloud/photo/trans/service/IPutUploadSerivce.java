package com.cloud.photo.trans.service;

import com.cloud.photo.api.common.bo.FileUploadBo;
import com.cloud.photo.api.common.common.CommonEnum;

public interface IPutUploadSerivce {

    String getPutUploadUrl(String fileName, String fileMd5, Long fileSize);


    CommonEnum commit(FileUploadBo bo);

    CommonEnum commitTransSecond(FileUploadBo bo);
}
