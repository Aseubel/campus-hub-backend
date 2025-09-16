package com.aseubel.campushubbackend.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.campushubbackend.common.util.AliOSSUtil;
import com.aseubel.campushubbackend.config.AppConfigProperties;
import com.aseubel.campushubbackend.context.UserContext;
import com.aseubel.campushubbackend.mapper.ImageMapper;
import com.aseubel.campushubbackend.pojo.dto.common.UploadImageRequest;
import com.aseubel.campushubbackend.pojo.entity.Image;
import com.aseubel.campushubbackend.pojo.entity.User;
import com.aseubel.campushubbackend.service.CommonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025/9/16 上午11:05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final AliOSSUtil aliOSSUtil;
    private final ImageMapper imageMapper;
    private final ThreadPoolTaskExecutor threadPoolExecutor;
    private final AppConfigProperties appConfigProperties;

    @Override
    public Image uploadImage(UploadImageRequest request) throws ClientException {
        Image image = request.toImage();
        return saveLocal(image);
    }

    private Image saveLocal(Image image) {
        MultipartFile file = image.getImage();
        try {
            byte[] bytes = file.getBytes();
            // 生成随机UUID作为文件名
            String id = IdUtil.fastSimpleUUID();
            image.setId(id);
            String finalFileName = id + "." + FileUtil.extName(file.getOriginalFilename());
            // 构建目标文件路径
            String targetFilePath = appConfigProperties.getFilePath() + finalFileName;
            image.setImageUrl(appConfigProperties.getGetFileApi() + finalFileName);
            image.setName(finalFileName);
            image.setUploadTime(LocalDateTime.now());
            imageMapper.insert(image);
            // 使用Hutool的IoUtil工具类将文件写入目标路径
            IoUtil.write(new FileOutputStream(targetFilePath), true, bytes);
            return image;
        } catch (IOException e) {
            // 使用日志框架记录错误信息
            log.error("上传图片失败", e);
            throw new RuntimeException("上传图片失败");
        }
    }

    private Image saveOSS(Image image) throws ClientException {
        String id = IdUtil.fastSimpleUUID();
        image.setId(id);
        image.ossUrl();
        aliOSSUtil.upload(image);
        imageMapper.insert(image);
        return image;
    }
}
