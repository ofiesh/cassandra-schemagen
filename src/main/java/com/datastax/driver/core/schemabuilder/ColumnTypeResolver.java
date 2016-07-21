package com.datastax.driver.core.schemabuilder;

import com.datastax.driver.core.DataType;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.InetAddresses;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

public class ColumnTypeResolver {
    final static ImmutableMap<Type, DataType> nativeTypes;

    static {
        nativeTypes = new ImmutableMap.Builder<Type, DataType>()
                .put(String.class, DataType.text())
                .put(Long.class, DataType.bigint())
                .put(long.class, DataType.bigint())
                .put(Boolean.class, DataType.cboolean())
                .put(boolean.class, DataType.cboolean())
                .put(BigDecimal.class, DataType.decimal())
                .put(double.class, DataType.cdouble())
                .put(Double.class, DataType.cdouble())
                .put(InetAddresses.class, DataType.inet())
                .put(Date.class, DataType.timestamp())
                .put(UUID.class, DataType.timeuuid())
                .put(Integer.class, DataType.cint())
                .put(int.class, DataType.cint())
                .build();
    }

    private static Type[] getGenericType(Field field) {
        return ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
    }

    public static ColumnType type(@NotNull Type type) {
        return type((Class) type);
    }

    public static ColumnType type(@NotNull Class type) {
        DataType dataType = nativeTypes.get(type);
        if(dataType != null) {
            return new NativeColumnType(dataType);
        }

        UDT udt = (UDT) type.getDeclaredAnnotation(UDT.class);
        if(udt != null && udt.name().length() > 0) {
            return UDTType.literal(udt.name());
        }

        throw new IllegalArgumentException(type.getTypeName() + " is not a valid cql type or UDT");
    }

    public static PublicColumnType type(@NotNull Field field) {
        if(Set.class.isAssignableFrom(field.getType())) {
            return ColumnTypeFactory.set(type(getGenericType(field)[0]));
        }

        if(Collection.class.isAssignableFrom(field.getType())) {
            return ColumnTypeFactory.list(type(getGenericType(field)[0]));
        }

        if(Map.class.isAssignableFrom(field.getType())) {
            Type[] generics = getGenericType(field);
            return ColumnTypeFactory.map(type(generics[0]), type(generics[1]));
        }

        return () -> type(field.getType()).asCQLString();
    }
}
