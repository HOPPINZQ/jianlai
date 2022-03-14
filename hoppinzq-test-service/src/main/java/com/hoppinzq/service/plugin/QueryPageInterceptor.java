package com.hoppinzq.service.plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.Properties;

/**
 * @author:ZhangQi
 **/
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class QueryPageInterceptor {
    protected Log log = LogFactory.getLog(this.getClass());

    public QueryPageInterceptor() {
    }

    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Object parameterObject = boundSql.getParameterObject();
//        if (parameterObject != null) {
//            page = convertParameter(parameterObject);
//        }
//
//        if (page != null && page.getPageSize() != -1) {
//            if (StringUtils.isBlank(boundSql.getSql())) {
//                return null;
//            }
//
//            String originalSql = boundSql.getSql().trim();
//            page.setTotal(SQLHelper.getCount(originalSql, (Connection)null, mappedStatement, parameterObject, boundSql, this.log));
//            String pageSql = APISQLHelper.generatePageSql(originalSql, page, this.DIALECT);
//            invocation.getArgs()[2] = new RowBounds(0, 2147483647);
//            BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
//            if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
//                MetaObject mo = (MetaObject)Reflections.getFieldValue(boundSql, "metaParameters");
//                Reflections.setFieldValue(newBoundSql, "metaParameters", mo);
//            }
//
//            MappedStatement newMs = this.copyFromMappedStatement(mappedStatement, new QueryPageInterceptor.BoundSqlSqlSource(newBoundSql));
//            invocation.getArgs()[0] = newMs;
//        }

        return invocation.proceed();
    }

//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    public void setProperties(Properties properties) {
//        super.initProperties(properties);
//    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null) {
            String[] var4 = ms.getKeyProperties();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String keyProperty = var4[var6];
                builder.keyProperty(keyProperty);
            }
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return this.boundSql;
        }
    }
}
