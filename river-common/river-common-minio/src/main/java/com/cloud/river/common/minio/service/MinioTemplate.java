package com.cloud.river.common.minio.service;

import com.cloud.river.common.minio.vo.MinioItem;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @program: RiverCloud
 * @description: minio 交互类
 * @author: River
 * @create: 2019-03-30 18:12
 **/
@RequiredArgsConstructor
public class MinioTemplate implements InitializingBean {
    private final String endpoint;
    private final String accessKey;
    private final String secretKey;
    private MinioClient client;

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    @SneakyThrows
    public void createBucket(String bucketName){
        if(!client.bucketExists(bucketName)){
            client.makeBucket(bucketName);
        }
    }

    @SneakyThrows
    public List<Bucket> getAllBuckets (){ return client.listBuckets();}

    @SneakyThrows
    public Optional<Bucket> getBucket(String bucketName) {
        return  client.listBuckets().stream().
                filter(bucket -> bucket.name() == bucketName).findFirst();
    }

    @SneakyThrows
    public void removeBucket(String bucketName) { client.removeBucket(bucketName); }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    @SneakyThrows
    public List<MinioItem> getAllObjectsByPrefix(String bucketName, String prefix, Boolean recursive){
        List<MinioItem> minioItems = new ArrayList<>();
        Iterable<Result<Item>> results = client.listObjects(bucketName, prefix, recursive);

        while (results.iterator().hasNext()){
            minioItems.add(new MinioItem(results.iterator().next().get()));
        }

        return minioItems;
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    过期时间 <=7
     * @return url
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName, Integer expires){
        return client.presignedGetObject(bucketName, objectName, expires);
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName){
        return client.getObject(bucketName, objectName);
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void pubObject(String bucketName, String objectName, InputStream stream) throws Exception{
        client.putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName  文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contextType 类型
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contextType)throws Exception{
        client.putObject(bucketName,objectName, stream, size, contextType);
    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    public ObjectStat getObjectInfo(String bucketName, String objectName) throws Exception{
        return client.statObject(bucketName, objectName);
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String bucketName, String objectName) throws Exception{
        client.removeObject(bucketName, objectName);
    }

    @Override
    public void afterPropertiesSet() throws Exception{
        Assert.hasText(endpoint, "Minio url为空");
        Assert.hasText(accessKey,"Minio accessKey为空");
        Assert.hasText(secretKey, "Minio secretKey为空");
        this.client = new MinioClient(endpoint, accessKey, secretKey);
    }
}
