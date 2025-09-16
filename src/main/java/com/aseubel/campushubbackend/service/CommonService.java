package com.aseubel.campushubbackend.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.campushubbackend.pojo.dto.common.UploadImageRequest;
import com.aseubel.campushubbackend.pojo.entity.Image;

/**
 * @author Aseubel
 * @date 2025/9/16 上午11:04
 */
public interface CommonService {

    Image uploadImage(UploadImageRequest request) throws ClientException;

}
