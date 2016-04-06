package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.mapping.MappingManager;

import java.io.Closeable;
import java.io.IOException;

public class DescribeAccessorTester implements Closeable {
    private final Cluster cluster;
    private final DescribeAccessor describeAccessor;

    public DescribeAccessorTester() {
        this.cluster = ClusterFactory.cluster();
        describeAccessor = new MappingManager(cluster.newSession()).createAccessor(DescribeAccessor.class);
    }

    public DescribeAccessor getDescribeAccessor() {
        return describeAccessor;
    }

    @Override
    public void close() {
        cluster.close();
    }
}
