package com.cloud.river.upms.api.vo;

import lombok.Data;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-26 10:44
 **/
@Data
public class LogVO {
    private String url;
    private String time;
    private String user;
    private String type;
    private String message;
    private String stack;
    private String info;
}
