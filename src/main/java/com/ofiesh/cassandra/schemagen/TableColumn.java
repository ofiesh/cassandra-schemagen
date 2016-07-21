package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "columns", keyspace = "system_schema")
public class TableColumn {
    @PartitionKey
    @Column(name = "keyspace_name")
    private String keyspaceName;
    @ClusteringColumn(0)
    @Column(name = "table_name")
    private String tableName;
    @ClusteringColumn(1)
    @Column(name = "column_name")
    private String columnName;

    public String getKeyspaceName() {
        return keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
