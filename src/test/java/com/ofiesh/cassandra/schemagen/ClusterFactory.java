package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.core.Cluster;

import static com.datastax.driver.core.Cluster.*;

public class ClusterFactory {

    public static Cluster cluster() {
        return builder().addContactPoint("localhost").build();
    }
}
