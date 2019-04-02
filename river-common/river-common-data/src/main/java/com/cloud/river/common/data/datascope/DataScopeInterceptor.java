package com.cloud.river.common.data.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.cloud.river.common.core.exception.CheckedException;
import com.cloud.river.common.data.enums.DataScopeTypeEnum;
import com.cloud.river.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: RiverCloud
 * @description: 数据权限拦截器
 * @author: River
 * @create: 2019-03-26 22:47
 **/
@Slf4j
@AllArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {
    public final DataSource dataSource;

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation){
        StatementHandler statementHandler = PluginUtils.realTarget(invocation);
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);

        MappedStatement mappedStatement =(MappedStatement)metaObject.getValue("delegate.mappedStatement");
        if(!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())){
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql)metaObject.getValue("delegate: boundSql");
        String originSql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();

        DataScope dataScope = findDataScopeObject(parameterObject);
        if(dataScope == null){
            return invocation.proceed();
        }

        String scopeName = dataScope.getScopeName();
        List<Integer> deptIds = dataScope.getDeptIds();
        if(CollUtil.isEmpty(deptIds)){
            List<Integer> roles = SecurityUtils.getRoles();
            if(CollUtil.isEmpty(roles)){
                throw new CheckedException("auto datascope, please setup org.springframework.security details true");
            }

            Entity query = Db.use(dataSource)
                    .query("select * from sys_role where role_id in (" + CollUtil.join(roles, ",") + ')')
                    .stream().min(Comparator.comparingInt(p -> p.getInt("ds_type"))).get();

            Integer dsType = query.getInt("ds_type");
            if(DataScopeTypeEnum.ALL.equals(dsType)){
                return invocation.proceed();
            }

            if(DataScopeTypeEnum.CUSTOM.equals(dsType)){
                String dsScope = query.getStr("ds_scope");
                deptIds.addAll(Arrays.stream(dsScope.split(","))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList()));
            }

            if(DataScopeTypeEnum.OWN_CHILD_LEVEL.equals(dsType)){
                deptIds = Db.use(dataSource)
                        .findBy("sys_dept_relation", "ancestor", SecurityUtils.getUser().getDeptId())
                        .stream().map(entity -> entity.getInt("descendant"))
                        .collect(Collectors.toList());
            }

            if(DataScopeTypeEnum.OWN_LEVEL.equals(dsType)){
                deptIds.add(SecurityUtils.getUser().getDeptId());
            }
        }

        String join = CollUtil.join(deptIds, ",");
        originSql = "select * from (" +originSql +") temp_data_scope where temp_data_scope." + scopeName +" IN (" + join +")";
        metaObject.setValue("delegate:boundSql.sql", originSql);
        return invocation.proceed();
    }

    public DataScope findDataScopeObject(Object parameterObj){
        if(parameterObj instanceof DataScope){
            return (DataScope)parameterObj;
        } else if(parameterObj instanceof Map){
            for(Object val: ((Map<?,?>) parameterObj).values()){
                if(val instanceof DataScope){
                    return (DataScope)val;
                }
            }
        }
        return null;
    }

    @Override
    public Object plugin(Object target){
        if(target instanceof StatementHandler){
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }
}
