package com.aseubel.campushubbackend.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.campushubbackend.common.ApiResponse;
import com.aseubel.campushubbackend.config.AppConfigProperties;
import com.aseubel.campushubbackend.pojo.dto.common.ImageResponse;
import com.aseubel.campushubbackend.pojo.dto.common.UploadImageRequest;
import com.aseubel.campushubbackend.pojo.entity.Image;
import com.aseubel.campushubbackend.service.CommonService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 通用方法控制器
 *
 * @author Aseubel
 * @date 2025/7/27 下午8:28
 */
@Slf4j
@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;
    private final AppConfigProperties appConfigProperties;

    /**
     * 上传图片
     *
     * @param request  上传图片请求
     * @param response 响应
     * @return ApiResponse
     * @throws ClientException OSS上传异常
     */
    @PostMapping("/uploadImage")
//    @RequireLogin
    public ApiResponse<ImageResponse> uploadImage(@ModelAttribute UploadImageRequest request,
                                                  HttpServletResponse response) throws ClientException, IOException {
        if (ObjectUtil.isEmpty(request.getFile()) || StrUtil.isEmpty(request.getFile().getOriginalFilename())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        Image image = commonService.uploadImage(request);
        return ApiResponse.success(new ImageResponse(image));
    }

    /**
     * 获取图片
     *
     * @param imgName 图片名称
     * @return 图片文件流
     */
    @GetMapping(value = "/file/{imgName}")
    public void getFile(@PathVariable String imgName, HttpServletResponse resp) {
        try {
            String filePath = appConfigProperties.getFilePath() + imgName;
            File file = new File(filePath);
            if (!file.exists()) {
                file = new File(appConfigProperties.getFilePath() + "default.jpg");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            // 零拷贝
            resp.setContentType(Files.probeContentType(file.toPath()));
            Files.copy(file.toPath(), resp.getOutputStream());
        } catch (IOException e) {
            // 使用日志框架记录错误信息
            log.error("Error reading image file: {}", e.getMessage());
        }
    }
}
