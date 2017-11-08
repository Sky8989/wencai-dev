package com.sftc.tools.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.DOUBLE)
public class NullDoubleHandler extends BaseTypeHandler<Double> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Double d, JdbcType jdbcType) throws SQLException {
        if (d == null) {
            preparedStatement.setDouble(i, 0);
        } else {
            preparedStatement.setDouble(i, d);
        }
    }

    public Double getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return resultSet.getDouble(s);
    }

    public Double getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getDouble(i);
    }

    public Double getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getDouble(i);
    }
}