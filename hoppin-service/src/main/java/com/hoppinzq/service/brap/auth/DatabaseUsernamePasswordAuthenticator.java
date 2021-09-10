package com.hoppinzq.service.brap.auth;


import com.hoppinzq.service.brap.common.InvocationRequest;
import com.hoppinzq.service.brap.common.UsernamePasswordPrincipal;
import com.hoppinzq.service.brap.exception.AuthenticationFailedException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 使用JDBC数据源执行身份验证的验证器
 * @see InvocationRequest#getCredentials() 通过该方法可以获取到调用方身份信息
 */
public class DatabaseUsernamePasswordAuthenticator implements AuthenticationProvider {
    private DataSource dataSource;
    private PreparedStatement cachedAuthStatement;
    
    public DatabaseUsernamePasswordAuthenticator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void authenticate(InvocationRequest invocationRequest) throws AuthenticationFailedException {
        if (invocationRequest.getCredentials() != null && invocationRequest.getCredentials() instanceof UsernamePasswordPrincipal) {
            try {
                UsernamePasswordPrincipal upp = (UsernamePasswordPrincipal) invocationRequest.getCredentials();

                PreparedStatement stmt = getAuthStatement();
                stmt.setString(0, upp.getUsername());
                stmt.setString(1, upp.getPassword());

                executeAndValidate(stmt, invocationRequest);
            } catch (SQLException e) {
                cachedAuthStatement = null;
                e.printStackTrace();
                throw new AuthenticationFailedException(e);
            }
        }
    }

    public void executeAndValidate(PreparedStatement stmt, InvocationRequest invocationRequest) throws SQLException, AuthenticationFailedException {
        if (stmt.executeQuery().next())
            AuthenticationContext.setPrincipal(invocationRequest.getCredentials());
        else
            throw new AuthenticationFailedException("身份验证失败");
    }

    private PreparedStatement getAuthStatement() throws SQLException {
        if (cachedAuthStatement == null)
            cachedAuthStatement = dataSource.getConnection().prepareStatement(getAuthSql());

        return cachedAuthStatement;
    }


    public String getAuthSql() {
        return "SELECT * FROM users WHERE username = ? AND password = ?";
    }

}
