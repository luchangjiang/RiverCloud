package com.cloud.river.upms.biz.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.constant.enums.LoginTypeEnum;
import com.cloud.river.common.core.util.R;
import com.cloud.river.upms.api.entity.SysUser;
import com.cloud.river.upms.biz.service.MobileService;
import com.cloud.river.upms.biz.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-28 19:58
 **/
@Slf4j
@Service
@AllArgsConstructor
public class MobileServiceImpl implements MobileService {
    private final RedisTemplate redisTemplate;
    private final SysUserService sysUserService;
    /**
     * 发送手机验证码
     *
     * @param mobile mobile
     * @return code
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile){
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getPhone, mobile));
        if(sysUser == null){
            return new R<>(Boolean.FALSE, "手机号未注册");
        }

        String key = CommonConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + "@" + mobile;
        Object obj = redisTemplate.opsForValue().get(key);
        if(obj != null){
            return new R<>(Boolean.FALSE, "该手机有难码还未过期");
        }

        String code = RandomUtil.randomNumbers(Integer.valueOf(SecurityConstants.CODE_SIZE));
        redisTemplate.opsForValue().set(key, code);

        return new R<>(Boolean.TRUE, code);
    }
}
