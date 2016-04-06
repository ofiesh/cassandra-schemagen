package com.datastax.driver.core.schemabuilder;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CreateTable {
    private final Create create;
    private final Field simple;
    private final Field clustering;
    private final Field partition;

    public CreateTable(String tableName) {
        create = new Create(tableName);
        try {
            simple = create.getClass().getSuperclass().getDeclaredField("simpleColumns");
            clustering = create.getClass().getDeclaredField("clusteringColumns");
            partition = create.getClass().getDeclaredField("partitionColumns");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateTable addColumn(String columnName, ColumnType columnType) {
        putColumn(columnName, columnType, simple);
        return this;
    }

    public CreateTable addClusteringColumn(String columnName, ColumnType columnType) {
        putColumn(columnName, columnType, clustering);
        return this;
    }

    public CreateTable addPartitionColumn(String columnName, ColumnType colomnType) {
        putColumn(columnName, colomnType, partition);
        return this;
    }

    public String buildInternal() {
        return create.buildInternal();
    }

    @SuppressWarnings("unchecked")
    private void putColumn(String columnName, ColumnType columnType, Field field) {
        try {
            field.setAccessible(true);
            HashMap<String, ColumnType> columnTypes = (HashMap<String, ColumnType>) field.get(create);
            columnTypes.put(columnName, columnType);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
