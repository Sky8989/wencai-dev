package com.sftc.tools.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.INTEGER)
public class NullIntegerHandler extends BaseTypeHandler<Integer> {

    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Integer integer, JdbcType jdbcType) throws SQLException {
        if (integer == null) {
            preparedStatement.setInt(i, 0);
        } else {
            preparedStatement.setInt(i, integer);
        }
    }

    public Integer getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return resultSet.getInt(s);
    }

    public Integer getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getInt(i);
    }

    public Integer getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getInt(i);
    }
}
