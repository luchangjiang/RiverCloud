package com.cloud.river.umps.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: RiverCloud
 * @description: menu vo
 * @author: River
 * @create: 2019-03-26 11:46
 **/
@Data
public class MenuVO implements Serializable {
    private static final long serialVersionID = 1L;

    private Integer menuId;
    private String name;
    private String permission;
    private Integer parentId;
    private String icon;
    private String path;
    private String component;
    private String keepAlive;
    private String type;
    private String label;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /**
     * 0--正常 1--删除
     */
    private String delFlag;

    @Override
    public int hashCode(){ return menuId.hashCode();}

    @Override
    public boolean equals(Object obj){
        if(obj instanceof MenuVO){
            Integer targetMenuId = ((MenuVO)obj).getMenuId();
            return this.menuId.equals(targetMenuId);
        }
        return false;
    }
}
