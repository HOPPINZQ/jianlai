package com.hoppinzq.service.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:ZhangQi
 **/
public class SQLUtil {

    private SQLUtil() {
    }

    /**
     * 判断SQL语句是否正确
     *
     * @param map
     * @param sql
     * @return
     */
    public static Boolean isSQLRight(Map<String, Object> map, String sql) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtil.getConnection(map);
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();
            while (rs.next()) {
                break;
            }
            return true;
        } catch (Exception ex) {
            return false;
        } finally {
            JDBCUtil.close(connection, statement, rs);
        }
    }

    /**
     * 判断查询结果集中是否存在某列
     *
     * @param rs         查询结果集
     * @param columnName 列名
     * @return true 存在; false 不存咋
     */
    public static boolean isExistColumn(ResultSet rs, String columnName) {
        try {
            if (rs.findColumn(columnName) > 0) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    /**
     * 提取多表查询的表名
     * @param SQL
     * @return
     */
    public static List<String> getTableBySQL(String SQL) {
        List<String> list = new ArrayList<>();
        SQL=SQL.toUpperCase();
        int fromIndex = SQL.split("FROM").length;
        String[] from = new String[fromIndex];
        from = SQL.split("FROM");
        int joinIndex = SQL.split("JOIN").length;
        String[] join = new String[joinIndex];
        join = SQL.split("JOIN");
        if(fromIndex>=2){
            for (int i = 1; i < fromIndex; i++) {
                list.add(from[i].trim().split(" ")[0]);
            }
        }
        if(joinIndex>=2){
            for (int i = 1; i < joinIndex; i++) {
                list.add(join[i].trim().split(" ")[0]);
            }
        }
        return list;
    }
}
