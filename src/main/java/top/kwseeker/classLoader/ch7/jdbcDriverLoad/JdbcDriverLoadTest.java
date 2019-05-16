package top.kwseeker.classLoader.ch7.jdbcDriverLoad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class JdbcDriverLoadTest {

    public static void main(String[] args) {
        try {
            //Class.forName()在加载接口前预先加载驱动实现类，JDBC4.0之后已经不需要了，因为有SPI拓展机制
            //Class.forName("com.mysql.cj.jdbc.Driver");

            //通过SPI直接加载驱动接口
            //DriverManager 位于lib/rt.jar由启动类加载器加载
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mybatis_learn?characterEncoding=utf8",
                    "root",
                    "123456");
            String querySql = "Select * from t_blog";
            ResultSet resultSet = conn.createStatement().executeQuery(querySql);
            resultSet.last();
            System.out.println(resultSet.getRow());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
