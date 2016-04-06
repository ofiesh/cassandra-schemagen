package com.datastax.driver.core.schemabuilder;

public class ColumnTypeFactory {

    public static PublicColumnType columnType(String columnType) {
        return () -> columnType;
    }

    public static ColumnType collectedType(ColumnType columnType) {
        return columnType instanceof NativeColumnType ? columnType :
                columnType("frozen<" + columnType.asCQLString() + ">");
    }

    public static PublicColumnType set(ColumnType columnType) {
        return columnType("set<" + collectedType(columnType).asCQLString() + ">");
    }

    public static PublicColumnType list(ColumnType columnType) {
        return columnType("list<" + collectedType(columnType).asCQLString() + ">");
    }

    public static PublicColumnType map(ColumnType key, ColumnType value) {
        return columnType("map<" + collectedType(key).asCQLString() + ", " + collectedType(value).asCQLString() + ">");
    }
}
