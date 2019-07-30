package lingxi.shop.upload.service;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lingxi.shop.common.enums.ExceptionEnum;
import lingxi.shop.common.exception.lingxiException;
import lingxi.shop.upload.config.UploadProperties;
import lingxi.shop.upload.web.UploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    @Autowired
    private FastFileStorageClient storageClient;
    @Autowired
    private UploadProperties prop;

    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!suffixes.contains(type)) {
                throw new lingxiException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new lingxiException(ExceptionEnum.IMAGE_NOT_FOND);
            }
            // 2、保存图片
            // 2.1、生成保存目录
            File dir = new File("D:\\xiamgmu\\upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 2.2、保存图片
            file.transferTo(new File(dir, file.getOriginalFilename()));

            // 2.3、拼接图片地址
            String url = "http://image.lingxi.com/upload/" + file.getOriginalFilename();

            return url;
        } catch (Exception e) {
            return null;

        }
    }
}
