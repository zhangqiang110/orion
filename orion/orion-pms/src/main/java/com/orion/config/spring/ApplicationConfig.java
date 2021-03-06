package com.orion.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.orion.config.datasource.DataSourceConfig;
import com.orion.config.springdata.JpaConfig;
import com.orion.config.springdata.MongodbConfig;
import com.orion.config.springdata.RabbitmqConfig;
import com.orion.config.springdata.RedisConfig;
import com.orion.config.springdata.SolrConfig;

/**
 * 该配置文件和其他几个springdata文件夹下的配置类， 就相当于xml配置中的 ApplicationContext.xml，
 * 只不过分开为不同的配置类中
 * 
 * @author John
 *
 */
@Configuration
@PropertySource("classpath:config.properties")
@Import(value={DataSourceConfig.class, JpaConfig.class, MongodbConfig.class, RabbitmqConfig.class, RedisConfig.class, SolrConfig.class})
@ComponentScan(basePackages = { "com.orion" }, excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class, ControllerAdvice.class }) })
@EnableAspectJAutoProxy(proxyTargetClass = true) // 支持切面,设置proxyTargetClass参数用来指定是使用CGLIB基于类的代理还是使用jdk基于接口的代理，使用@Aspect注解
@EnableTransactionManagement // 支持事务,使用@Transational注解
//@EnableScheduling // 支持定时任务,使用@Scheduled注解(如果使用，单独放在一个类中配置，并配置close方法)
//@EnableAsync // 支持异步,使用@Async注解(如果使用，单独放在一个类中配置，并配置close方法)
public class ApplicationConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
