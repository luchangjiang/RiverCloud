package com.cloud.river.common.minio.vo;

import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @program: RiverCloud
 * @description: minio对象信息
 * @author: River
 * @create: 2019-03-30 17:54
 **/
@Data
@AllArgsConstructor
public class MinioItem {
    private String objectName;
    private Date lastModified;
    private String etag;
    private Long size;
    private String storageClass;
    private Owner owner;
    private String type;

    public MinioItem(Item item){
        this.objectName = item.objectName();
        this.lastModified = item.lastModified();
        this.etag = item.etag();
        this.size = (long) item.size();
        this.storageClass = item.storageClass();
        this.owner = item.owner();
        this.type = item.isDir() ? "directory" : "file";
    }
}
