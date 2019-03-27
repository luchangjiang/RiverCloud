package com.cloud.river.umps.api.vo;

import com.cloud.river.umps.api.entity.SysRole;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: RiverCloud
 * @description: user vo
 * @author: River
 * @create: 2019-03-26 17:19
 **/
@Data
public class UserVO implements Serializable {
    public static final long serialVersionUID = 1L;

    private Integer userId;
    private String username;
    private String password;
    private String salt;
    private String wxOpenid;
    private String qqOpendId;
    private String phone;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String delFlag;
    private String lockFlag;
    private String avatar;
    private Integer deptId;
    private String deptName;
    private Integer tenantId;
    private List<SysRole> roles;


}
