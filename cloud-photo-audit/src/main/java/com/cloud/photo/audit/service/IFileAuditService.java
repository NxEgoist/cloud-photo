package com.cloud.photo.audit.service;

import com.cloud.photo.audit.entity.FileAudit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.photo.api.common.bo.AuditPageBo;

/**
 * <p>
 * 文件审核列表 服务类
 * </p>
 *
 * @author egoist
 * @since 2023-07-15
 */
public interface IFileAuditService extends IService<FileAudit> {
    Boolean updateAuditStatus(AuditPageBo pageBo);
}
