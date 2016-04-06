package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
interface DescribeAccessor {
    @Query("SELECT keyspace_name FROM system.schema_keyspaces WHERE keyspace_name = :name")
    ResultSet getSchemaKeyspace(@Param("name") String keyspaceName);

    @Query("SELECT keyspace_name, columnfamily_name FROM system.schema_columnfamilies " +
            "WHERE keyspace_name = :name AND columnfamily_name = :columnFamilyName")
    ResultSet getSchemaColumnFamily(@Param("name") String keyspaceName, @Param("columnFamilyName") String columnFamilyName);

    @Query("SELECT keyspace_name, columnfamily_name FROM system.schema_columnfamilies WHERE keyspace_name = :name")
    ResultSet getSchemaColumnFamilies(@Param("name") String keyspaceName);

    @Query("SELECT keyspace_name, columnfamily_name, column_name FROM system.schema_columns "
            + "WHERE keyspace_name = :keyspaceName AND columnfamily_name = :columnFamilyName AND column_name = :columnName")
    ResultSet getSchemaColumn(@Param("keyspaceName") String keyspaceName,
                              @Param("columnFamilyName") String columnFamilyName,
                              @Param("columnName") String columnName);

    @Query("SELECT keyspace_name, columnfamily_name, column_name FROM system.schema_columns "
            + "WHERE keyspace_name = :keyspaceName AND columnfamily_name = :columnFamilyName")
    ResultSet getSchemaColumns(@Param("keyspaceName") String keyspaceName,
                               @Param("columnFamilyName") String columnFamilyName);

    @Query("SELECT field_names FROM system.schema_usertypes "
            + "WHERE keyspace_name = :keyspaceName AND type_name = :typeName")
    ResultSet getUDTFields(@Param String keyspaceName, @Param String typeName);
}