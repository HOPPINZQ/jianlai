package com.hoppinzq.service.html;


import com.hoppinzq.service.log.Log;
import com.hoppinzq.service.spiderService.IWorkloadStorable;

import java.sql.*;

/**
 * @author: zq
 */
public class SpiderSQLWorkload implements IWorkloadStorable {
    Connection _connection;
    PreparedStatement _prepClear;
    PreparedStatement _prepAssign;
    PreparedStatement _prepGetStatus;
    PreparedStatement _prepSetStatus1;
    PreparedStatement _prepSetStatus2;
    PreparedStatement _prepSetStatus3;

    public SpiderSQLWorkload(String driver, String sqlConnect) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        this._connection = DriverManager.getConnection(sqlConnect);
        this._prepClear = this._connection.prepareStatement("DELETE FROM tblWorkload;");
        this._prepAssign = this._connection.prepareStatement("SELECT URL FROM tblWorkload WHERE Status = 'W';");
        this._prepGetStatus = this._connection.prepareStatement("SELECT Status FROM tblWorkload WHERE URL = ?;");
        this._prepSetStatus1 = this._connection.prepareStatement("SELECT count(*) as qty FROM tblWorkload WHERE URL = ?;");
        this._prepSetStatus2 = this._connection.prepareStatement("INSERT INTO tblWorkload(URL,Status) VALUES (?,?);");
        this._prepSetStatus3 = this._connection.prepareStatement("UPDATE tblWorkload SET Status = ? WHERE URL = ?;");
    }

    public synchronized String assignWorkload() {
        ResultSet resultSet = null;

        String temp;
        try {
            resultSet = this._prepAssign.executeQuery();
            if (!resultSet.next()) {
                Object o = null;
                return (String)o;
            }

            String url = resultSet.getString("URL");
            this.setStatus(url, 'R');
            temp = url;
        } catch (SQLException sqlException) {
            Log.logException("SQL Error: ", sqlException);
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception exception) {
            }

        }

        return temp;
    }

    public synchronized void addWorkload(String url) {
        if (this.getURLStatus(url) == 'U') {
            this.setStatus(url, 'W');
        }
    }

    public synchronized void completeWorkload(String url, boolean isE) {
        if (isE) {
            this.setStatus(url, 'E');
        } else {
            this.setStatus(url, 'C');
        }

    }

    protected void setStatus(String str, char c) {
        ResultSet resultSet = null;

        try {
            this._prepSetStatus1.setString(1, str);
            resultSet = this._prepSetStatus1.executeQuery();
            resultSet.next();
            int qty = resultSet.getInt("qty");
            if (qty < 1) {
                this._prepSetStatus2.setString(1, str);
                this._prepSetStatus2.setString(2, (new Character(c)).toString());
                this._prepSetStatus2.executeUpdate();
            } else {
                this._prepSetStatus3.setString(1, (new Character(c)).toString());
                this._prepSetStatus3.setString(2, str);
                this._prepSetStatus3.executeUpdate();
            }
        } catch (SQLException sqlException) {
            Log.logException("SQL Error: ", sqlException);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception exception) {
            }

        }

    }

    public synchronized char getURLStatus(String url) {
        ResultSet resultSet = null;

        char c;
        try {
            this._prepGetStatus.setString(1, url);
            resultSet = this._prepGetStatus.executeQuery();
            if (!resultSet.next()) {
                char c1 = 'U';
                return c1;
            }

            c = resultSet.getString("Status").charAt(0);
        } catch (SQLException sqlException) {
            Log.logException("SQL Error: ", sqlException);
            return 'U';
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception exception) {
            }

        }

        return c;
    }

    public synchronized void clear() {
        try {
            this._prepClear.executeUpdate();
        } catch (SQLException sqlException) {
            Log.logException("SQL Error: ", sqlException);
        }

    }
}
