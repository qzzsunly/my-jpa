package cn.sexycode.myjpa.samples;

//import cn.sexycode.myjpa.data.repository.support.MyjpaRepositoryFactoryBean;

import cn.sexycode.myjpa.data.repository.support.MyjpaRepositoryFactoryBean;
import cn.sexycode.myjpa.samples.dao.UserDao;
import cn.sexycode.myjpa.samples.model.User;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

//import org.mybatis.spring.annotation.MapperScan;

/**
 *
 * @author qinzaizhen
 */
@SpringBootApplication/*(exclude = MyJpaAutoConfiguration.class)*/
//@EnableJpaRepositories(repositoryFactoryBeanClass = MyjpaRepositoryFactoryBean.class)
@EnableJpaRepositories(repositoryFactoryBeanClass = MyjpaRepositoryFactoryBean.class)
@MapperScan("cn.sexycode.mybatis.jpa.samples.dao")
public class MyJpaSamplesApplication {
    public static void main(String[] args) {
        UserDao userDao = SpringApplication.run(MyJpaSamplesApplication.class, args).getBeanFactory()
                .getBean(UserDao.class);
        List<User> byFullName = userDao.findByFullName("1");
        //        User user = (User) byFullName;
        //        System.out.println("user: " +user);
        //User.findByFullName
        System.out.println("user: " + byFullName);
    }
}