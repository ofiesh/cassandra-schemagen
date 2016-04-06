package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.core.schemabuilder.Udt;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.collect.ImmutableSet;

import java.math.BigDecimal;
import java.util.*;

@Table(name = "table_name")
public class EntityComplexKey {
    @PartitionKey(0)
    private UUID id;

    @PartitionKey(1)
    @Column(name = "update_id")
    private UUID updateId;

    @ClusteringColumn(0)
    private String name;

    @ClusteringColumn(1)
    private Integer ordering;

    private BigDecimal total;

    @Column(name = "set_text")
    private ImmutableSet<String> setText;

    private HashMap<String, BigDecimal> map;

    @Column
    private Udt fooooo;
}
