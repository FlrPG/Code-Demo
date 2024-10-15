//package com.flr.config;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//
//@Configuration
//public class FlywayConfig {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @PostConstruct
//    public void config() {
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .baselineOnMigrate(true)
//                .locations("db/migration")
//                .baselineVersion("1.0.1")
//                .cleanDisabled(true)
//                .load();
//        flyway.migrate();
//    }
//}