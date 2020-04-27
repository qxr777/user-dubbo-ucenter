package edu.whut.cs.jee.ucenter.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源（数据源切换，讀寫分離）
 * @author qixin
 */
@Configuration
public class DataSourceConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private Environment props;

    /**
     * basic setting
     */
    private DruidDataSource abstractDataSource() {
        DruidDataSource abstractDataSource = new DruidDataSource();
        abstractDataSource.setDriverClassName(props.getProperty("dataSource.driverClass"));
        abstractDataSource.setTestOnBorrow(true);
        abstractDataSource.setTestWhileIdle(true);
        abstractDataSource.setValidationQuery("SELECT 1");
        abstractDataSource.setMinEvictableIdleTimeMillis(30000);
        abstractDataSource.setPoolPreparedStatements(true);
        abstractDataSource.setMaxOpenPreparedStatements(100);
        return abstractDataSource;
    }

    /**
     * maste setting
     */
    @Bean(destroyMethod = "close", name="master")
    @Primary
    public DruidDataSource masterDataSource() {
        DruidDataSource masterDataSource = abstractDataSource();
        masterDataSource.setUrl(props.getProperty("dataSource.master.url"));
        masterDataSource.setUsername(props.getProperty("dataSource.master.username"));
        masterDataSource.setPassword(props.getProperty("dataSource.master.password"));
        masterDataSource.setMaxActive(Integer.parseInt(props.getProperty("dataSource.master.maxActive")));
        masterDataSource.setMinIdle(Integer.parseInt(props.getProperty("dataSource.master.minIdle")));
        return masterDataSource;
    }

    /**
     * slave setting
     */
    @Bean(destroyMethod = "close", name="slave")
    public DruidDataSource slaveDataSource() {
        DruidDataSource slaveDataSource = abstractDataSource();
        slaveDataSource.setUrl(props.getProperty("dataSource.slave.url"));
        slaveDataSource.setUsername(props.getProperty("dataSource.slave.username"));
        slaveDataSource.setPassword(props.getProperty("dataSource.slave.password"));
        slaveDataSource.setMaxActive(Integer.parseInt(props.getProperty("dataSource.slave.maxActive")));
        slaveDataSource.setMinIdle(Integer.parseInt(props.getProperty("dataSource.slave.minIdle")));
        return slaveDataSource;
    }

    @Bean(name="dynamicDataSource")
    public DataSource dynamicDataSource() throws IOException {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource());
        targetDataSources.put("slave", slaveDataSource());

        AbstractRoutingDataSource dynamicDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                String lookupKey = TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "slave" : "master";
                LOGGER.info("当前操作使用的数据源：{}", lookupKey);
                return lookupKey;
            }
        };

        dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get("master"));
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }

    @Bean
    public DataSource dataSource() throws IOException {
        return new LazyConnectionDataSourceProxy(dynamicDataSource());
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) throws IOException {
        return builder
                .dataSource(dataSource())
                .packages("edu.whut.cs.jee.ucenter")
                .build();
    }

    @Bean(name = "transactionManager")
    JpaTransactionManager transactionManager(EntityManagerFactoryBuilder builder) throws IOException {
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }
}
