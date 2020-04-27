package edu.whut.cs.jee.ucenter.web;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author qixin
 */
@SpringBootApplication//(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableJpaRepositories
@EnableTransactionManagement
@ComponentScan(basePackages = "edu.whut.cs.jee.ucenter")
@EnableDubboConfiguration
public class UserSpringBootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserSpringBootWebApplication.class, args);
    }

}
