package com.hoppinzq.service.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * @author:ZhangQi
 **/
public class DBUtil {
    private static Logger logger = LogManager.getLogger();
    public static Connection initOracle(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //连接数据库
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
        }catch(Exception se) {
            logger.error("ORACLE连接失败",se);
            return null;
        }
        return conn;
    }

    public static Connection initDB2(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Driver driver=(Driver) Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
            //连接数据库
            DriverManager.registerDriver(driver);
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
        }catch(Exception se) {
            logger.error("DB2连接失败",se);
            return null;
        }
        return conn;
    }
    public static Connection initMysql(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
        }catch(Exception se) {
            conn = null;
            logger.error("MYSQL连接失败",se);
            return null;
        }
        return conn;
    }

    public static Connection initSQLServer(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
        } catch (Exception se) {
            logger.error("SQLSERVER连接失败",se);
            return null;
        }
        return conn;
    }


    public static Connection initGreenPlum(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Class.forName("com.pivotal.jdbc.GreenplumDriver");
            conn = DriverManager.getConnection(jdbcurl,username, password);
            conn.setAutoCommit(autoCommit);
        } catch (Exception se) {
            logger.error("GREENPLUM连接失败",se);
            return null;
        }
        return conn;
    }

    public static Connection initHIVE(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
            conn = DriverManager.getConnection(jdbcurl,username, password);
            conn.setAutoCommit(autoCommit);
        } catch (Exception se) {
            logger.error("HIVE连接失败",se);
            return null;
        }
        return conn;
    }
    public static Connection intiConnection(String jdbcdriver,String jdbcurl, String username, String password, boolean autoCommit, String dbtype) {
        Connection conn = null;
        try {
            Class.forName(jdbcdriver);
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
        } catch (Exception se) {
            logger.error("连接失败",se);
            return null;
        }
        return conn;
    }

    public static void closeDB(Connection conn, PreparedStatement pstmt,ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            rs = null;

            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;

            if (conn != null) {
                conn.close();
            }
            conn = null;
        } catch (Exception ee) {
            logger.error("数据源关闭失败",ee);
        }
    }

}