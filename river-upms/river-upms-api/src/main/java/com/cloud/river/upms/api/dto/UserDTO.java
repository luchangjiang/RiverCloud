package com.cloud.river.upms.api.dto;

import com.cloud.river.upms.api.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-26 22:38
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {
    private List<Integer> roleIds;
    private Integer deptId;
    private String newPassword;
}
