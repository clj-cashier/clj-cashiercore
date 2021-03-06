/**
 * fshows.com
 * Copyright (C) 2013-2018 All Rights Reserved.
 */
package com.apricot.clj.cashiercore.service.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ctrip.framework.apollo.core.enums.Env;
import com.apricot.framework.extend.transaction.DefaultFsTransactionManager;
import com.apricot.framework.extend.transaction.FsTransactionManager;
import com.mysql.jdbc.Driver;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author auto
 * @version SpringMybatisConfig.java, v 0.1 2018-06-13 14:08 auto
 */
@Configuration
public class SpringMybatisConfig {
    @Autowired
    private SysConfig sysConfig;

    /**
     * Mapper接口所在包名，Spring会自动查找其下的类
     *
     * @return
     */
    @Bean
    public static MapperScannerConfigurer 1Configurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.apricot.clj.cashiercore.service.dal.1");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("1SqlSessionFactory");
        return mapperScannerConfigurer;
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public DruidDataSource 1DataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        //  基本属性 url、user、password
        druidDataSource.setUrl(sysConfig.getJdbcUrl());
        druidDataSource.setUsername(sysConfig.getJdbcUsername());
        druidDataSource.setPassword(sysConfig.getJdbcPassword());
        druidDataSource.setDriverClassName(Driver.class.getName());
        //  配置初始化大小、最小、最大
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(64);
        //  配置获取连接等待超时的时间
        druidDataSource.setMaxWait(60000);
        //  配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        //  配置一个连接在池中最小生存的时间，单位是毫秒
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery(sysConfig.getJdbcPoolValidationQuery());
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        //  打开PSCache，并且指定每个连接上PSCache的大小
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        //  配置监控统计拦截的filters
        String env = System.getProperty("env", "UNKNOWN");
        if(!StringUtils.isEmpty(env) && Env.PRO == Env.valueOf(env.trim())){
            druidDataSource.setFilters("wall");
        }else{
            druidDataSource.setFilters("wall,log4j2");
        }
        return druidDataSource;
    }

    /**
     * spring和MyBatis完美整合，不需要mybatis的配置映射文件
     *
     * @return
     */
    @Bean
    public SqlSessionFactoryBean 1SqlSessionFactory() throws IOException, SQLException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(1DataSource());
        // 设置查找器
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/com.apricot/clj/cashiercore/service/dal/1/mapper/*.xml"));
        return sqlSessionFactoryBean;
    }

    /**
     * (事务管理)transaction manager, use JtaTransactionManager for global tx
     *
     * @return
     */
    @Bean
    public DataSourceTransactionManager 1TransactionManager() throws SQLException {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(1DataSource());
        return dataSourceTransactionManager;
    }

    /**
     * 创建自定义事务模版
     *
     * @return
     */
    @Bean
    public FsTransactionManager fs1TransactionManager() throws SQLException {
        DefaultFsTransactionManager fsTransactionManager = new DefaultFsTransactionManager();
        fsTransactionManager.setTransactionManager(1TransactionManager());
        return fsTransactionManager;
    }
}