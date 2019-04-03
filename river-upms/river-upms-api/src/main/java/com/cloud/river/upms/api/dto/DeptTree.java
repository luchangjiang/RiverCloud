package com.cloud.river.upms.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: RiverCloud
 * @description: dept tree
 * @author: River
 * @create: 2019-03-26 11:33
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode {
    private String name;
}
