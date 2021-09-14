package com.hoppinzq.service.util;

import java.sql.*;
import java.util.Map;

/**
 * @author:ZhangQi
 **/
public class JDBCUtil {
    private JDBCUtil(){}
    private static String SQLUseSSL="?useSSL=false";
    private static Connection con ;

    public static Connection getConnection(Map<String,Object> map){
        try{
            if(map.get("sql_sqltype").toString().toUpperCase().equals("MYSQL")){
                Class.forName("com.mysql.jdbc.Driver");//暂时提供这两个驱动
            }else{
                Class.forName("oracle.jdbc.driver.OracleDriver");
            }
            String url = map.get("sql_datasource").toString()+SQLUseSSL;
            String username=map.get("sql_username").toString();
            String password=map.get("sql_password").toString();
            con = DriverManager.getConnection(url, username, password);
        }catch(Exception ex){
            throw new RuntimeException(ex+"连接"+map.get("sql_type")+"数据库失败");
        }
        return con;
    }

    public static Connection getBaseConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ceshi";
            String username="root";
            String password="19zhangqi";
            con = DriverManager.getConnection(url, username, password);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return con;
    }
    public static Connection getBaseBabyConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/baby";
            String username="root";
            String password="19zhangqi";
            con = DriverManager.getConnection(url, username, password);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return con;

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
