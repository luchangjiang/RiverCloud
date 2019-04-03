package com.cloud.river.upms.api.dto;

import com.cloud.river.upms.api.entity.SysUser;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: RiverCloud
 * @description: user、roles、permissions
 * @author: River
 * @create: 2019-03-27 14:51
 **/
@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private SysUser sysUser;
    private Integer[] roles;
    private String[] permissions;

}
