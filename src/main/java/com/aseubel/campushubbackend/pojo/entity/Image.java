package com.aseubel.campushubbackend.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.aseubel.campushubbackend.common.Constant.*;

/**
 * @author Aseubel
 * @date 2025/9/16 上午10:59
 */
@TableName("image")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @TableId(type = IdType.AUTO)
    private String id;
    private String imageUrl;
    private String name;
    private LocalDateTime uploadTime;
    @TableField(exist = false)
    private MultipartFile image;

    /**
     * 获取在OSS中的文件名称（在类型文件夹下）
     */
    public String imageObjectName() {
        StringBuilder objectName = new StringBuilder();
        objectName.append(APP).append("/")
                .append(id)
                .append(Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf(".")));
        return objectName.toString();
    }

    /**
     * 获取oss的url
     */
    public String ossUrl() {
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(BUCKET_NAME)
                .append(".")
                .append(ENDPOINT)
                .append("/")
                .append(imageObjectName());
        this.imageUrl = stringBuilder.toString();
        return this.imageUrl;
    }
}
