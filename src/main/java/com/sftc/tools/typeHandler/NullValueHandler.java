package com.sftc.tools.typeHandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NullValueHandler implements TypeHandler<String> {

    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }

    public void setParameter(PreparedStatement pstmt, int index, String value, JdbcType jdbcType) throws SQLException {
        if (value == null) {
            if (jdbcType == JdbcType.VARCHAR) {
                // 字符串设置当前参数的值为空字符串
                pstmt.setString(index, "");
            } else if (jdbcType == JdbcType.INTEGER || jdbcType == JdbcType.DOUBLE || jdbcType == JdbcType.NUMERIC) {
                // 数字设置当前参数的值为0
                pstmt.setInt(index,0);
            }
        } else {
            // 如果不为null，则直接设置参数的值为value
            pstmt.setString(index, value);
        }
    }
}