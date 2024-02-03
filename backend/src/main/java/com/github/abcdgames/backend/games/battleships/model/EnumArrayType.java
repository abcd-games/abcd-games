package com.github.abcdgames.backend.games.battleships.model;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.*;
import java.util.Arrays;

public class EnumArrayType implements UserType<BattleshipField[][]> {

    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    public Class<BattleshipField[][]> returnedClass() {
        return BattleshipField[][].class;
    }

    @Override
    public boolean equals(BattleshipField[][] x, BattleshipField[][] y) {
        return Arrays.deepEquals(x, y);
    }

    @Override
    public int hashCode(BattleshipField[][] x) {
        return Arrays.deepHashCode(x);
    }

    @Override
    public BattleshipField[][] nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        Array array = rs.getArray(position);

        if (array == null) {
            return null;
        }

        Object[] arrayData = (Object[]) array.getArray();

        BattleshipField[][] battleshipFields = new BattleshipField[arrayData.length][];
        for (int i = 0; i < arrayData.length; i++) {
            Object[] innerArray = (Object[]) arrayData[i];
            battleshipFields[i] = new BattleshipField[innerArray.length];

            for (int j = 0; j < innerArray.length; j++) {
                if (innerArray[j] != null) {
                    battleshipFields[i][j] = BattleshipField.valueOf((String) innerArray[j]);
                }
            }
        }

        return battleshipFields;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, BattleshipField[][] value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null || value.length == 0) {
            st.setNull(index, Types.NULL);
            return;
        }

        try (Connection connection = session.getJdbcConnectionAccess().obtainConnection()) {
            st.setArray(index, connection.createArrayOf("BATTLESHIP_FIELD", value));
        }
    }

    @Override
    public BattleshipField[][] deepCopy(BattleshipField[][] value) {
        if (value == null) {
            return null;
        }

        BattleshipField[][] copy = new BattleshipField[value.length][];
        for (int i = 0; i < value.length; i++) {
            copy[i] = Arrays.copyOf(value[i], value[i].length);
        }

        return copy;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(BattleshipField[][] value) {
        return value;
    }

    @Override
    public BattleshipField[][] assemble(Serializable cached, Object owner) {
        return (BattleshipField[][]) cached;
    }
}
