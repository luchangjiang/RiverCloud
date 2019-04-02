package com.cloud.river.auth.endpoint;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.river.common.core.constant.CommonConstants;
import com.cloud.river.common.core.constant.PaginationConstants;
import com.cloud.river.common.core.constant.SecurityConstants;
import com.cloud.river.common.core.util.R;
import com.cloud.river.common.data.tenant.TenantContextHolder;
import com.cloud.river.common.security.annotation.Inner;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-04-02 11:48
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/token")
public class RiverTokenEndpoint {
    private static final String RIVER_OAUTH_ACCESS = SecurityConstants.RIVER_PREFIX + SecurityConstants.OAUTH_PREFIX + "auth_to_access;";
    private final TokenStore tokenStore;
    private final RedisTemplate redisTemplate;
    private final CacheManager cacheManager;

    /**
     * 认证页面
     *
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView require(){ return new ModelAndView("ftl/login"); }

    /**
     * 退出token
     *
     * @param authHeader Authorization
     */
    @DeleteMapping("/logout")
    public R logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if(StrUtil.isBlank(authHeader)){
            return R.builder().code(CommonConstants.FAIL)
                    .msg("退出失败，token 为空").build();
        }

        String tokenValue = authHeader.replace("Bearer","").trim();
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if(accessToken == null || StrUtil.isBlank(accessToken.getValue())){
            return R.builder().code(CommonConstants.FAIL)
                    .msg("退出失败，token 无效").build();
        }

        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
        cacheManager.getCache("user_details").evict(oAuth2Authentication.getName());
        tokenStore.removeAccessToken(accessToken);
        return new R<>(Boolean.TRUE);
    }

    /**
     * 令牌管理调用
     *
     * @param token token
     * @return
     */
    @Inner
    @DeleteMapping("/{token}")
    public R deleteToken(@RequestParam(value="token") String token){
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        tokenStore.removeAccessToken(accessToken);
        return new R<>(Boolean.TRUE);
    }

    /**
     * 查询token
     *
     * @param params 分页参数
     * @return
     */
    @Inner
    @GetMapping("/page")
    public R getPage(@RequestBody Map<String, Object> params){
        //根据分页参数获取对应数据
        String key = String.format("%s*:%s", RIVER_OAUTH_ACCESS, TenantContextHolder.getTenantId());

        int current = MapUtil.getInt(params, PaginationConstants.CURRENT);
        int size = MapUtil.getInt(params, PaginationConstants.CURRENT);
        List<String> pages = findKeysForPage(key, current, size);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());

        Page result = new Page(current, size);
        result.setRecords(redisTemplate.opsForValue().multiGet(pages));
        result.setTotal(Long.valueOf(redisTemplate.keys(key).size()));
        return new R<>(result);
    }

    private List<String> findKeysForPage(String patternKey, int pageNum, int pageSize){
        ScanOptions scanOptions = ScanOptions.scanOptions().match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) redisTemplate.executeWithStickyConnection(redisConnection ->
                new ConvertingCursor<>(redisConnection.scan(scanOptions), redisSerializer::deserialize));
        List<String> result = new ArrayList<>();
        int tmpIndex=0;
        int startIndex =(pageNum-1) * pageSize;
        int end = pageNum * pageSize;

        assert cursor!=null;
        while (cursor.hasNext()){
            if(tmpIndex>=startIndex && tmpIndex<end){
                result.add(cursor.next().toString());
                tmpIndex++;
                continue;
            }
            if(tmpIndex>=end){
                break;
            }
            tmpIndex++;
            cursor.next();
        }
        return result;
    }

}
