package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "tables", keyspace = "system_schema")
public class SystemTable {
    @PartitionKey
    @Column(name = "keyspace_name")
    private String keyspaceName;
    @ClusteringColumn
    @Column(name = "table_name")
    private String tableName;

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
}
