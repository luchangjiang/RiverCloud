package com.cloud.river.umps.api.feign;

import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.constant.ServiceNameConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.umps.api.dto.UserInfo;
import com.cloud.river.umps.api.entity.SysUser;
import com.cloud.river.umps.api.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(value = ServiceNameConstants.RIVER_BIZ)
public interface RemoteUserService {

    /**
     * 通过用户名查询用户、角色信息
     *
     * @param username 用户名
     * @param from     调用标志
     * @return R
     */
    @GetMapping("/user/info/{username}")
    R<UserInfo> info(@PathVariable String username, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 通过社交账号或手机号查询用户、角色信息
     *
     * @param loginStr LoginTypeEnum@code
     * @param from  调用标志
     * @return
     */
    @GetMapping("/social/info/{loginStr}")
    R<UserInfo> social(@PathVariable String loginStr, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R
     */
    @GetMapping("/user/ancestor/{username}")
    R<List<SysUser>> ancestor(@PathVariable String username);
}
