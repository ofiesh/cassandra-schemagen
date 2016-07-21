package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "keyspaces", keyspace = "system_schema")
public class Keyspace {

    @PartitionKey
    @Column(name = "keyspace_name")
    private String keyspaceName;

    public String getKeyspaceName() {
        return keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }
}
