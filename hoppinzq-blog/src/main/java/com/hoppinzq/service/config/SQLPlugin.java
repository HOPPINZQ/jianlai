package com.hoppinzq.service.config;


import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.jdbc.PreparedStatementLogger;
import org.apache.ibatis.logging.jdbc.StatementLogger;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Properties;

@Intercepts({@Signature(
        type= StatementHandler.class,
        method = "parameterize",
        args = {Statement.class})})
public class SQLPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable{
        Object returnObj=invocation.proceed();
        Statement statement=(Statement)invocation.getArgs()[0];
        if(Proxy.isProxyClass(statement.getClass())){
            MetaObject metaObject= SystemMetaObject.forObject(statement);
            Object h=metaObject.getValue("h");
            if(h instanceof StatementLogger){
                RoutingStatementHandler routingStatementHandler=(RoutingStatementHandler)invocation.getTarget();
                System.err.println(routingStatementHandler.getBoundSql().getSql());
            }else{
                PreparedStatementLogger preparedStatementLogger=(PreparedStatementLogger)h;
                PreparedStatement preparedStatement=preparedStatementLogger.getPreparedStatement();
                System.err.println(preparedStatement.toString());
            }
        }else{
            System.err.println(statement.toString());
        }

        return returnObj;
    }

    @Override
    public Object plugin(Object o) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
