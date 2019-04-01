package com.cloud.river.umps.biz.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.log.annotation.Sys_Log;
import com.cloud.river.common.minio.service.MinioTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: RiverCloud
 * @description: 文件管理
 * @author: River
 * @create: 2019-04-01 21:09
 **/
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final MinioTemplate minioTemplate;

    /**
     * 上传文件
     * 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
     * @param file 资源
     * @return R(bucketName, filename)
     */
    @Sys_Log("上传文件")
    @PostMapping("/uplodad")
    public R uploadFile(@RequestParam("file")MultipartFile file){
        String fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("bucketName", CommonConstants.BUCKET_NAME);
        resultMap.put("fileName", fileName);

        try {
            minioTemplate.pubObject(CommonConstants.BUCKET_NAME, fileName, file.getInputStream());
        } catch (Exception e) {
            log.error("上传失败", e);
            return R.builder().code(CommonConstants.FAIL)
                    .msg(e.getLocalizedMessage()).build();
        }
        return R.builder().data(resultMap).build();
    }

    /**
     * 获取文件
     *
     * @param fileName 文件空间/名称
     * @param response
     * @return
     */
    @GetMapping("/{fileName}")
    public void getFile(@RequestParam("fileName") String fileName, HttpServletResponse response){
        String[] nameArray = fileName.split(StrUtil.DASHED);
        InputStream inputStream = minioTemplate.getObject(nameArray[0], nameArray[1]);

        try {
            response.setContentType("application/octet-stream; charset=UTF-8");
            IoUtil.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            log.error("文件读取异常", e);
        }
    }
}
