package com.datastax.driver.core.schemabuilder;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.UdtDataType;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.InetAddresses;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

public class DataTypeResolver {
    private final static ImmutableMap<Type, DataType> nativeTypes;

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

    public static DataType type(Type type) {
        return type((Class) type);
    }

    public static DataType type(Class type) {
        DataType dataType = nativeTypes.get(type);
        if(dataType != null) {
            return dataType;
        }

        UDT udt = (UDT) type.getDeclaredAnnotation(UDT.class);
        if(udt != null && udt.name().length() > 0) {
            return new UdtDataType(udt.name());
        }

        throw new IllegalArgumentException(type.getTypeName() + " is not a valid cql type or UDT");
    }

    public static DataType type(Field field) {
        if(field.getType().isEnum()) {
            return DataType.text();
        }

        if(Set.class.isAssignableFrom(field.getType())) {
            DataType dataType = type(getGenericType(field)[0]);
            return DataType.set(dataType);
        }

        if(Collection.class.isAssignableFrom(field.getType())) {
            DataType dataType = type(getGenericType(field)[0]);
            return DataType.list(dataType);
        }

        if(Map.class.isAssignableFrom(field.getType())) {
            Type[] generics = getGenericType(field);
            DataType key = type(generics[0]);
            DataType value = type(generics[1]);
            return DataType.map(key, value);
        }

        return type(field.getType());
    }
}
