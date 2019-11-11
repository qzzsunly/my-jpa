package cn.sexycode.myjpa.boot.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * @author qinzaizhen
 */
@Configuration
@ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class, EntityManager.class, JpaRepository.class})
@EnableConfigurationProperties(JpaProperties.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@Import({ MyjpaConfiguration.class, JpaRepositoriesAutoConfigureRegistrar.class })
@ConditionalOnBean(DataSource.class)
/*@ConditionalOnMissingBean({JpaRepositoryFactoryBean.class,
        JpaRepositoryConfigExtension.class})*/
@ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MyjpaAutoConfiguration {

}