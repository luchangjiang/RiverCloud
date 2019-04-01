package com.cloud.river.common.minio.http;

import com.cloud.river.common.minio.service.MinioTemplate;
import com.cloud.river.common.minio.vo.MinioItem;
import com.cloud.river.common.minio.vo.MinioObject;
import io.minio.messages.Bucket;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: RiverCloud
 * @description: minio 对外提供服务端点
 * @author: River
 * @create: 2019-03-31 21:24
 **/
@ConditionalOnProperty(value = "minio.endpoint.enabled", havingValue = "true")
@RestController
@AllArgsConstructor
@RequestMapping("${minio.endpoint.name:/minio}")
public class MinioEndpoint {
    private final MinioTemplate minioTemplate;

    @SneakyThrows
    @PostMapping("/bucket/{bucketName}")
    public Bucket createBucket(@PathVariable String bucketName){
        minioTemplate.createBucket(bucketName);
        return minioTemplate.getBucket(bucketName).get();
    }

    @SneakyThrows
    @GetMapping("/bucket")
    public List<Bucket> getAllBuckets(){
        return minioTemplate.getAllBuckets();
    }

    @SneakyThrows
    @GetMapping("/bucket/{buckName}")
    public Bucket getBucket(@PathVariable String buckName){
        return minioTemplate.getBucket(buckName)
                .orElseThrow(()->new IllegalArgumentException("Bucket name not found!"));
    }

    @SneakyThrows
    @DeleteMapping("/delete/{bucketName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBucket(@PathVariable String buckName){
        minioTemplate.removeBucket(buckName);
    }

    @SneakyThrows
    @PostMapping("/object/{bucketName}")
    public MinioObject createObject(@RequestBody MultipartFile object, @PathVariable String bucketName){
        String objectName = object.getOriginalFilename();
        minioTemplate.putObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
        return new MinioObject(minioTemplate.getObjectInfo(bucketName, objectName));
    }

    @SneakyThrows
    @PostMapping("/object/{bucketName}/{objectName}")
    public MinioObject createObject(@RequestBody MultipartFile object, @PathVariable String bucketName, @PathVariable String objectName){
        minioTemplate.putObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
        return new MinioObject(minioTemplate.getObjectInfo(bucketName, objectName));
    }

    @SneakyThrows
    @GetMapping("/object/{bucketName}/{prefix}")
    public List<MinioItem> filterObject(@PathVariable String bucketName, @PathVariable String prefix){
        return minioTemplate.getAllObjectsByPrefix(bucketName, prefix, true);
    }

    @SneakyThrows
    @GetMapping("/object/{bucketName}/{objectName}/{expires}")
    public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName, @PathVariable Integer expires){
        Map<String, Object> responseBody = new HashMap<>(8);
        responseBody.put("bucketName", bucketName);
        responseBody.put("objectName", objectName);
        responseBody.put("url", minioTemplate.getObjectUrl(bucketName, objectName, expires));
        responseBody.put("expires", expires);

        return responseBody;
    }

    @SneakyThrows
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/object/{bucketName}/{objectName}")
    public void delObject(@PathVariable String bucketName, @PathVariable String objectName){
        minioTemplate.removeObject(bucketName, objectName);
    }
}
