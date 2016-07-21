package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;

@Table(name = "types", keyspace = "system_schema")
public class SchemaType {
    @PartitionKey
    @Column(name = "keyspace_name")
    private String keyspaceName;
    @ClusteringColumn
    @Column(name = "type_name")
    private String typeName;
    @Column(name = "field_names")
    private List<String> fieldNames;

    public String getKeyspaceName() {
        return keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldNames(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }
}
