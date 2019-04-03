package com.cloud.river.upms.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.river.upms.api.entity.SysMenu;
import com.cloud.river.upms.api.vo.MenuVO;

import java.util.List;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 15:35
 **/
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<MenuVO> listMenusByRoleId(Integer roleId);

    List<String> listPermissionsByRoleIds(String roleIds);

}
