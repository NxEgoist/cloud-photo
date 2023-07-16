package com.cloud.photo.api.common.feign;


import com.cloud.photo.api.common.bo.FileAnalyzeBo;
import com.cloud.photo.api.common.bo.FileResizeIconBo;
import com.cloud.photo.api.common.common.ResultBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description:
 * tran服务接口
 * @author weifucheng
 * @date 2023/6/14 4:57 下午
 */
@FeignClient(value = "cloud-photo-image" )
@Service
public interface CloudPhotoImageService {

    /**
     * 获取缩略图地址
     * @param fileResizeIconBo
     * @return
     */
    @RequestMapping("/image/getIconUrl")
    ResultBody getIconUrl(@RequestBody FileResizeIconBo fileResizeIconBo);

    /**
     * 获取文件格式信息
     * @param fileAnalyzeBo
     * @return
     */
    @RequestMapping("/image/getMediaInfo")
    ResultBody getMediaInfo(@RequestBody FileAnalyzeBo fileAnalyzeBo);

}
