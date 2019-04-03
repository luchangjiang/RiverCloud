package com.cloud.river.common.data.tenant;

import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: RiverCloud
 * @description:
 * @author: River
 * @create: 2019-03-27 10:59
 **/
@Slf4j
public class RiverTenantHandler implements TenantHandler {
    @Autowired
    private RiverTenantConfigProperties properties;

    @Override
    public Expression getTenantId(){
        return new LongValue(TenantContextHolder.getTenantId());
    }

    @Override
    public String getTenantIdColumn(){
        return properties.getColumn();
    }

    @Override
    public boolean doTableFilter(String tableName){
        return !properties.getTables().contains(tableName);
    }
}
