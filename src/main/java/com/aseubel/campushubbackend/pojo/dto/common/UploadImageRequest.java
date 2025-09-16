package com.aseubel.campushubbackend.pojo.dto.common;

import com.aseubel.campushubbackend.common.annotation.FieldDesc;
import com.aseubel.campushubbackend.pojo.entity.Image;
import com.aseubel.campushubbackend.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/7/27 下午8:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageRequest implements Serializable {

    @FieldDesc(name = "图片本体")
    private MultipartFile file;

    public Image toImage() {
        Image image = new Image();
        image.setImage(file);
        image.ossUrl();
        return image;
    }
}
