package com.cloud.photo.image.service;

import com.cloud.photo.image.entity.FileResizeIcon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 图片缩略图 服务类
 * </p>
 *
 * @author egoist
 * @since 2023-07-13
 */
public interface IFileResizeIconService extends IService<FileResizeIcon> {

    String getIconUrl(String userId, String fileId, String iconCode);

    void imageThumbnailAndMediaInfo(String storageObjectId, String fileName);
}
