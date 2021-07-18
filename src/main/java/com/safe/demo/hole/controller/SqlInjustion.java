package com.safe.demo.hole.controller;

import com.safe.demo.hole.controller.hibernate.dao.HibernateDao;
import com.safe.demo.hole.controller.hibernate.dao.HibernateDaoImpl;
import com.safe.demo.hole.mapper.SqlInjustionMapper;
import com.safe.demo.hole.controller.hibernate.pojo.HibernateEmp;
import com.safe.demo.hole.pojo.UserM;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@Api(tags = "sql注入集合")
public class SqlInjustion {
    @Resource
    private SqlInjustionMapper sqlInjustionMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HibernateDao hibernateDao;

    @Autowired
    private HibernateDaoImpl hibernateDaoImpl;


    /**
     * mybaits
     * 动态拼接字段、表名、关键字的都不行
     * 不能使用#{}形式预防sql注入
     * select * from
     * user where 1=if((ascii(substr(database() from 1 for 1))),1,0)
     * limit 0,1
     *
     * poc
     * /mybatis_special_sql_inject?table=user%20where%201=if((ascii(substr(database()%20from%201%20for%201))),1,0)
     */
    @ApiOperation(value = "mybatis框架特性下的sql注入url")
    @ApiImplicitParam(name = "table", value = "表名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/mybatis_special_sql_inject")
    public List<UserM> mybatis_special_sql_inject(String table) {
        System.out.println(table);
        return sqlInjustionMapper.selectList(table);
    }


    /**
     * mybaits
     * sql注入模拟 使用${}
     * s' or 1=1-- q
     *
     * poc:
     * /mybatis_sql_inject?name=s' or 1=1-- q
     */
    @ApiOperation(value = "mybatis框架下的${}使用不当造成sql注入的url")
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/mybatis_sql_inject")
    public List<UserM> mybatis_sql_inject(String name) {
        System.out.println(name);
        return sqlInjustionMapper.selectList2(name);
    }

    /**
     * mybatis
     * sql注入预防模式 使用#{}
     *
     * url:
     *  /mybatis_sql_protect?name=s' or 1=1-- q
     */
    @ApiOperation(value = "mybatis框架下的sql注入预防url")
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/mybatis_sql_protect")
    public List<UserM> mybatis_sql_protect(String name) {
        System.out.println(name);
        return sqlInjustionMapper.selectList3(name);
    }

    /**
     * hibernate
     * sql注入模拟
     *
     * url:
     * /hibernate_sql_inject?ename=aaaa' or 1=1-- q
     */
    @ApiOperation(value = "hibernate框架中的sql注入url")
    @ApiImplicitParam(name = "ename", value = "姓名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/hibernate_sql_inject")
    public List<HibernateEmp> hibernate_sql_inject(@RequestParam(required = false) String ename) {
        return hibernateDaoImpl.findEmpByNo(ename);
    }


    /**
     * hibernate
     * sql注入防护
     *
     * poc:
     * /hibernate_sql_protect?ename=aaaa' or 1=1-- q
     */
    @ApiOperation(value = "hibernate框架中的sql注入预防")
    @ApiImplicitParam(name = "ename", value = "姓名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/hibernate_sql_protect")
    public List<HibernateEmp> hibernate_sql_protect(@RequestParam(required = false) String ename) {
        return hibernateDao.findEmpByNo2(ename);
    }

    /**
     * hibernate
     * sql注入预防
     * poc:
     *  /hibernate_sql_protect2?ename=aaaa' or 1=1-- q
     */
    @ApiOperation(value = "hibernate框架中的sql注入预防2")
    @ApiImplicitParam(name = "ename", value = "姓名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/hibernate_sql_protect2")
    public List<HibernateEmp> hibernate_sql_protect2(@RequestParam(required = false) String ename) {
        return hibernateDao.findEmpByNo3(ename);
    }


    /**
     * sql注入模拟 拼接sql语句
     *
     * poc：
     * /jdbc_sql_inject?name=sdf%27%20or%201=1%20--%20q
     */
    @ApiOperation(value = "拼接sql语句下的sql注入url")
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String", paramType = "query")
    @GetMapping(value = "/jdbc_sql_inject")
    public List<UserM> jdbc_sql_inject(String name) {
        Connection connection = null;
        Statement statement = null;
        List<UserM> studentList = new ArrayList<UserM>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_jdbc", "root", "123456");
            statement = connection.createStatement();
            String sql = "select * from user where sname = '" + name + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                UserM student = new UserM();
                student.setId(resultSet.getString(1));
                student.setUsername(resultSet.getString(2));
                student.setPassword(resultSet.getString(3));
                studentList.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return studentList;
    }

    /**
     * sql注入预防模拟 使用预编译
     *
     * url：
     * /jdbc_sql_protect?name=sdf' or 1=1-- q
     *
     */
    @ApiOperation(value = "预编译预防sql注入url")
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "String", paramType = "query")
    @GetMapping("/jdbc_sql_protect")
    public List<UserM> jdbc_sql_protect(String name) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_jdbc", "root", "123456");
        String sql = "select * from user where sname = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<UserM> studentList = new ArrayList<UserM>();
        while (resultSet.next()) {
            UserM student = new UserM();
            student.setId(resultSet.getString(1));
            student.setUsername(resultSet.getString(2));
            student.setPassword(resultSet.getString(3));
            studentList.add(student);
        }
        preparedStatement.close();
        connection.close();
        return studentList;
    }

    /**
     * 预编译使用不当造成sql注入模拟
     * <p>
     * select * from
     * information_schema.tables where table=database() union select user.*,1,2,3,4,5,6,7,8,9,10,11,12,131,14,15,16 from user
     * where sname =
     * 'sdf'
     *
     * poc:
     *  /prepare_sql_inject?table=information_schema.tables where table_schema=database() union select user.*,database(),2,3,4,5,6,7,8,9,10,11,12,13,14,15,16 from user&sname='sdf'
     */
    @ApiOperation(value = "预编译使用不当下的sql注入url")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "table", value = "数据库表名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sname", value = "姓名", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/prepare_sql_inject")
    public List prepare_sql_inject(String table, String sname) throws ClassNotFoundException, SQLException {

        String sql = "select * from " + table + " where sname = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, sname);
//        String sql = "select * from "+ table;
//        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps;
    }


    /**
     * mysql-connect-jdbc.jar组件漏洞，此次测试版本为5.1.40
     *
     * 影响范围
     *
     * mysql-connector-java-5.1.18 ~ 5.1.40
     *
     * mysql-connector-java-6.x
     *
     * mysql-connector-java-8.0.7以上
     */
    @GetMapping("/jdbc_connect_attack")
    public void jdbc_connect_attack(String dburl,String username,String password) throws ClassNotFoundException, SQLException {
        String driverClass = "com.mysql.jdbc.Driver";
        String db_url = "jdbc:mysql://"+dburl;
        Class.forName(driverClass);
        DriverManager.getConnection(db_url,username,password);
    }

    /**
     * mysql模拟服务器文件位置src/main/resources/poc/jdbc-serializable-poc.py
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        /**
         * ServerStatusDiffInterceptor触发：
         */

        // mysql8.x
//        String driver = "com.mysql.cj.jdbc.Driver";
//        String DB_URL = "jdbc:mysql://127.0.0.1:3309/mysql?characterEncoding=utf8&useSSL=false&queryInterceptors=com.mysql.cj.jdbc.interceptors.ServerStatusDiffInterceptor&autoDeserialize=true";//8.x使用

        // mysql5.x
        String driver = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://127.0.0.1:3309/test?characterEncoding=utf8&useSSL=false&autoDeserialize=true&statementInterceptors=com.mysql.jdbc.interceptors.ServerStatusDiffInterceptor";

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(DB_URL);
    }
}
