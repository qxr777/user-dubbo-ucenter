package edu.whut.cs.jee.ucenter;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

/**
 * @author qixin
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableCaching
@EnableDubboConfiguration
public class UserSpringBootServiceApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(UserSpringBootServiceApplication.class, args);

        args = new String[] { "spring", "myjetty" };
        com.alibaba.dubbo.container.Main.main(args);

//        SpringApplication.run(UserSpringBootServiceApplication.class, args);
//        System.in.read(); // press any key to exit
    }


}
