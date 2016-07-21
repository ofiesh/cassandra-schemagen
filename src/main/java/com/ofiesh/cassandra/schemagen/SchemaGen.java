package com.ofiesh.cassandra.schemagen;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.annotations.*;

import java.lang.reflect.Field;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static com.datastax.driver.core.schemabuilder.ColumnTypeResolver.type;

public class SchemaGen {

    private final Session session;
    private final Mapper<Keyspace> keyspaceMapper;
    private final Mapper<SchemaType> schemaTypeMapper;
    private final Mapper<SystemTable> systemTableMapper;
    private final Mapper<TableColumn> tableColumnMapper;

    public SchemaGen(final Session session) {
        this.session = session;
        MappingManager mappingManager = new MappingManager(session);
        keyspaceMapper = mappingManager.mapper(Keyspace.class);
        schemaTypeMapper = mappingManager.mapper(SchemaType.class);
        systemTableMapper = mappingManager.mapper(SystemTable.class);
        tableColumnMapper = mappingManager.mapper(TableColumn.class);
    }

    public void generate(String keyspaceName, Class... entities) {
        Keyspace keyspace = keyspaceMapper.get(keyspaceName);
        if(keyspace == null) {
            session.execute("CREATE KEYSPACE " + keyspaceName + " WITH replication " +
                    "= {'class':'SimpleStrategy', 'replication_factor':1};");
        }

        session.execute("USE " + keyspaceName);

        Collection<String> tableStatements = new ArrayList<>();
        Collection<String> udtStatements = new ArrayList<>();

        for (Class entity: entities) {
            if(entity.getDeclaredAnnotation(Table.class) != null) {
                tableStatements.addAll(generateTable(keyspaceName, entity));
            } else if(entity.getDeclaredAnnotation(UDT.class) != null) {
                udtStatements.addAll(generateUDT(keyspaceName, entity));
            }
        }

        udtStatements.forEach(session::execute);
        tableStatements.forEach(session::execute);
    }

    public Collection<String> generateUDT(String keyspace, Class entity) {
        UDT udt = (UDT) entity.getDeclaredAnnotation(UDT.class);

        if(udt == null || udt.name().length() < 1) {
            throw new IllegalArgumentException("Cannot create udt for class with no udt name");
        }

        SchemaType type = schemaTypeMapper.get(keyspace, udt.name());
        if(type == null) {
            return Collections.singleton(createUDT(entity).buildInternal());
        } else {
            Collection<String> alterUdts = new ArrayList<>();
            for(Field field : entity.getDeclaredFields()) {
                String name = SchemaGenHelpers.getFieldName(field);
                if(!type.getFieldNames().contains(name)) {
                    alterUdts.add("ALTER TYPE " + udt.name() + " ADD " + name + " " + type(field).asCQLString());
                }
            }
            return alterUdts;
        }
    }

    public Collection<String> generateTable(String keyspace, Class entity) {
        Table table = (Table) entity.getDeclaredAnnotation(Table.class);

        if (table == null || table.name().length() < 1) {
            throw new IllegalArgumentException("Cannot create table for class with no table name");
        }

        SystemTable systemTable = systemTableMapper.get(keyspace, table.name());
        if (systemTable == null) {
            return Collections.singleton(create(entity).buildInternal());
        } else {
            return Arrays.stream(entity.getDeclaredFields())
                    .filter(field -> tableColumnMapper.get(keyspace, table.name(), getName(field)) == null)
                    .map(field -> PublicSchemaStatement.buildInternal(addColumn(table.name(), field)))
                    .collect(Collectors.toList());
        }
    }

    private static String getName(final Field field) {
        final Column column = field.getDeclaredAnnotation(Column.class);
        return column != null && column.name().length() > 0 ?
                column.name() : field.getName();
    }

    public static SchemaStatement dropColumn(String table, Field field) {
        return SchemaBuilder.alterTable(table).dropColumn(getName(field));
    }

    public static SchemaStatement addColumn(String table, Field field) {
        String name = getName(field);
        PublicColumnType columnType = type(field);

        return PublicSchemaStatement.fromQueryString("ALTER TABLE " + table + " ADD " + name + " "
                + columnType.asCQLString());
    }

    /***
     *  Makes a @{code Create} statement for a datastax annotated class.
     *  The class must have an @{code @Table} annotation and at least one @{code @PartitionKey}
     *
     * @param c class to generate a create statement for
     * @throws IllegalArgumentException if a name is not defined in the @{code @Table} annotation or if a cql type cannot be
     *      found for the field type
     * @return Returns a Datastax @{code Create} statement
     */
    public static Create create(final Class c) {
        final Table table = (Table) c.getDeclaredAnnotation(Table.class);

        if (table == null) {
            throw new IllegalArgumentException("Cannot create table for class, missing @Table annotation");
        }
        if (table.name().length() < 1) {
            throw new IllegalArgumentException("Cannot create table for class with no table name");
        }

        final Create create = SchemaBuilder.createTable(table.name());
        for (final Field field : c.getDeclaredFields()) {
            final Transient trans = field.getDeclaredAnnotation(Transient.class);
            if(trans == null) {
                DataType dataType = DataTypeResolver.type(field);

                final String name = getName(field);

                final PartitionKey partitionKey = field.getDeclaredAnnotation(PartitionKey.class);
                if(partitionKey != null) {
                    create.addPartitionKey(name, dataType);
                } else {
                    ClusteringColumn clusteringColumn = field.getDeclaredAnnotation(ClusteringColumn.class);
                    if(clusteringColumn != null) {
                        create.addClusteringColumn(name, dataType);
                    } else {
                        create.addColumn(name, dataType);
                    }
                }
            }
        }
        return create;
    }

    public static CreateType createUDT(Class clazz) {
        final UDT udt = (UDT) clazz.getDeclaredAnnotation(UDT.class);

        if (udt == null || udt.name().length() == 0) {
            throw new IllegalArgumentException("Cannot create UDT with no name");
        }

        CreateType type = SchemaBuilder.createType(udt.name());
        for (Field field : clazz.getDeclaredFields()) {
            DataType dataType = DataTypeResolver.type(field);

            type.addColumn(SchemaGenHelpers.getFieldName(field), dataType);
        }

        return type;
    }
}
