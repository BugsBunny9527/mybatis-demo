package com.mybatis.test;

import com.mybatis.domain.Employee;
import com.mybatis.mapper.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Luke Wang on 2018/8/30.
 */
public class MyBatisTest {

    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);

        return sqlSessionFactory;
    }

    /**
     * HelloWord 编写流程：
     * 1、编写全局配置文件， 有数据源一些运行环境信息
     * 2、编写sql映射文件；配置了每一个sql，以及sql的封装规则等。
     * 3、编写pojo
     * 4、将sql映射文件注册在全局配置文件中
     * 5、写代码：
     * 		1）、根据全局配置文件得到SqlSessionFactory；
     * 		2）、使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
     * 			一个sqlSession就是代表和数据库的一次会话，用完关闭
     * 		3）、使用sql的唯一标识来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件中的。
     * @throws IOException
     */
    @Test
    public void test01() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory =
                new SqlSessionFactoryBuilder().build(inputStream);

        // 2、获取sqlSession实例，能直接执行已经映射的sql语句
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            // sql的唯一标识：statement Unique identifier matching the statement to use.
            // 执行sql要用的参数：parameter A parameter object to pass to the statement.
            Employee employee = sqlSession.selectOne("com.mybatis.mapper.EmployeeMapper.getEmpById", 1);
            System.out.println(employee.toString());
        }finally {
            sqlSession.close();
        }


    }

    /**
     * 1、接口式编程
     * 	原生：		接口 XxxDao		====>  XxxDaoImpl
     * 	mybatis：	接口 XxxMapper	====>  XxxMapper.xml
     *
     * 2、SqlSession代表和数据库的一次会话；用完必须关闭；
     * 3、SqlSession和connection一样她都是非线程安全。每次使用都应该去获取新的对象。
     * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
     * 		（将接口和xml进行绑定）
     * 		EmployeeMapper empMapper =	sqlSession.getMapper(EmployeeMapper.class);
     * 5、两个重要的配置文件：
     * 		mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
     * 		sql映射文件：保存了每一个sql语句的映射信息：
     * 					将sql抽取出来。
     *
     */
    @Test
    public void test02() throws IOException {
        // 1、获取sqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = this.getSqlSessionFactory();
        // 2、获取sqlSession对象
        SqlSession openSession = sqlSessionFactory.openSession();

        try {
            // 3、获取接口的实现类对象
            //会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1);
            System.out.println(mapper.getClass());
            System.out.println(employee);
        } finally {
            openSession.close();
        }

    }
}
