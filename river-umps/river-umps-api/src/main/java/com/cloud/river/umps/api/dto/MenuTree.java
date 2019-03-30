package com.cloud.river.umps.api.dto;

import com.cloud.river.umps.api.vo.MenuVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: RiverCloud
 * @description: MenuNode extends from TreeNode
 * @author: River
 * @create: 2019-03-26 11:41
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode{
    private String icon;
    private String name;
    private Boolean spread = false;
    private String path;
    private String component;
    private String authority;
    private String redirect;
    private String keepAlive;
    private String type;
    private String code;
    private String label;
    private Integer sort;

    public MenuTree(){}

    public MenuTree(MenuVO menuVO){
        this.id = menuVO.getMenuId();
        this.parentId = menuVO.getParentId();
        this.name = menuVO.getName();
        this.icon = menuVO.getIcon();
        this.path = menuVO.getPath();
        this.keepAlive = menuVO.getKeepAlive();
        this.type = menuVO.getType();
        this.component = menuVO.getComponent();
        this.label = menuVO.getLabel();
        this.sort = menuVO.getSort();
    }
}
