package com.cloud.photo.trans.service;

import com.baomidou.mybatisplus.extension.service.IService;

public interface IGetDownloadService {
    String getDownloadUrlByFileId(String userId, String fileId);

    String getDownloadUrl(String containerId, String objectId);
}
