package com.hoppinzq.service.util;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author:ZhangQi
 * 数据库连接池
 **/
public class DBPoolUtil {
    private DBPoolUtil(){}

    private static Connection connection ;
    private static Context context;
    private static DataSource dataSource;

    public static Connection getConnection(){
        try{
            context=new InitialContext();
            //通过lookup()方法获取数据源，实例化一个数据源
            dataSource=(DataSource) context.lookup("java:comp/env/jdbc/emps");
            //通过数据源的getConnection()方法获取连接
            connection=dataSource.getConnection();
        }catch (NamingException ex){
            ex.printStackTrace();
        }catch (Exception ex){}
        return connection;
    }


    public static void close(Connection con, Statement stat){

        if(stat!=null){
            try{
                stat.close();
            }catch(SQLException ex){}
        }

        if(con!=null){
            try{
                con.close();
            }catch(SQLException ex){}
        }

    }

    public static void close(Connection con,Statement stat , ResultSet rs){
        if(rs!=null){
            try{
                rs.close();
            }catch(SQLException ex){}
        }

        if(stat!=null){
            try{
                stat.close();
            }catch(SQLException ex){}
        }

        if(con!=null){
            try{
                con.close();
            }catch(SQLException ex){}
        }

    }
}
