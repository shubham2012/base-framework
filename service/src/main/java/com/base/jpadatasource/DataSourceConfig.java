package com.base.jpadatasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataSourceConfig {

    private static final String PRIMARY_DATASOURCE_PREFIX = "primary";
    private static final String REPLICA_DATASOURCE_PREFIX = "replica";
    private static final long CONNECTION_TIME_OUT = 30000;

    @Value("${spring.datasource.hikari.idleTimeout:600000}")
    private long idealTimeOut;

    @Value("${spring.datasource.hikari.maxLifetime:1800000}")
    private long maxLifeTime;

    private static final int BATCH_SIZE = 50;
    private final Environment environment;

    /**
     * Compiling the data sources for Primary and replica sources
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        final RoutingDataSource routingDataSource = new RoutingDataSource();

        final DataSource primaryDataSource = buildDataSource("PrimaryHikariPool", PRIMARY_DATASOURCE_PREFIX);
        final DataSource replicaDataSource = buildDataSource("ReplicaHikariPool", REPLICA_DATASOURCE_PREFIX);

        final Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(RoutingDataSource.Route.PRIMARY, primaryDataSource);
        targetDataSources.put(RoutingDataSource.Route.REPLICA, replicaDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource);

        return routingDataSource;
    }

    /**
     * Data source builder
     *
     * @param poolName
     * @param dataSourcePrefix
     * @return
     */
    private DataSource buildDataSource(String poolName, String dataSourcePrefix) {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(poolName);
        hikariConfig.setJdbcUrl(environment.getProperty(String.format("%s.spring.datasource.url", dataSourcePrefix)));
        hikariConfig.setUsername(
                environment.getProperty(String.format("%s.spring.datasource.username", dataSourcePrefix)));
        hikariConfig.setPassword(
                environment.getProperty(String.format("%s.spring.datasource.password", dataSourcePrefix)));
        hikariConfig.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        hikariConfig.setMaximumPoolSize(
                Integer.valueOf(environment.getProperty(String.format("%s.pool.size.datasource", dataSourcePrefix))));
        hikariConfig.setMaxLifetime(maxLifeTime);
        hikariConfig.setIdleTimeout(idealTimeOut);
        hikariConfig.setConnectionTimeout(CONNECTION_TIME_OUT);

        Properties properties = new Properties();
        properties.setProperty("hibernate.jdbc.batch_size", String.valueOf(BATCH_SIZE));
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");

        hikariConfig.setDataSourceProperties(properties);

        return new HikariDataSource(hikariConfig);
    }
}
