package com.tomergabel.examples.eventsourcing.persistence;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.Sources;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.skife.jdbi.v2.DBI;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.distribution.Version.v5_7_latest;

class MysqlEventStoreTest extends EventStoreSpec {

    static EmbeddedMysql embeddedMysql;
    static DBI database;
    static MysqlEventStore store;

    @BeforeAll
    static void setup() {
        embeddedMysql =
                anEmbeddedMysql(v5_7_latest)
                        .addSchema("events", Sources.fromString(MysqlEventStore.SCHEMA_DDL))
                        .start();
        database = new DBI(
                "jdbc:mysql://localhost:" + embeddedMysql.getConfig().getPort() + "/events?useSSL=false",
                embeddedMysql.getConfig().getUsername(),
                embeddedMysql.getConfig().getPassword());
        MysqlEventStore.configureDatabase(database);
        store = new MysqlEventStore(database);
    }

    @AfterAll
    static void tearDown() {
        embeddedMysql.stop();
    }

    @Override
    protected EventStore getStore() {
        return store;
    }
}