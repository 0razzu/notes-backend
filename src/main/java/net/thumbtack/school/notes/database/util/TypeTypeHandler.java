package net.thumbtack.school.notes.database.util;


import net.thumbtack.school.notes.model.Type;
import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class TypeTypeHandler extends EnumTypeHandler<Type> {
    public TypeTypeHandler(Class<Type> type) {
        super(type);
    }
    
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Type parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name().toLowerCase());
    }
    
    
    @Override
    public Type getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Type.valueOf(rs.getString(columnName).toUpperCase());
    }
    
    
    @Override
    public Type getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Type.valueOf(rs.getString(columnIndex).toUpperCase());
    }
    
    
    @Override
    public Type getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Type.valueOf(cs.getString(columnIndex).toUpperCase());
    }
}
