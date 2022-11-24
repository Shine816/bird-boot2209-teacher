package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC java数据库连接  Java Database Connectivity
 * JAVA在JDBC中提供一套通用的接口，用于连接和操作数据库。不同的数据库厂商都提供了一套对应的实现类来操作自家提供的
 * DBMS。而这套实现类也称为连接该DBMS的驱动(Driver)
 *
 * 使用步骤:
 * 1:加载对应数据库厂商提供的驱动(MAVEN添加依赖即可)
 * 2:基于标准的JDBC操作流程操作该数据库
 */
public class JDBCDemo1 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        /*
            标准的JDBC操作流程:
            1:加载驱动:Class.forName("不同厂商提供的Driver的实现类")
              不同厂商提供的Driver实现类的名字不同，但是每个数据库是固定的。

            2:与数据库建立连接。使用DriverManager.getConnection()

            3:通过Connection创建执行对象Statement
              Statement用于指定SQL语句

            4:使用Statement执行SQL(CRUD)
              如果执行的是DQL语句，则还要接收查询结果集并遍历获取查询结果。
         */
        //1
        Class.forName("com.mysql.cj.jdbc.Driver");

        //2
        /*
            DriverManager.getConnection(String url,String username,String password)
            参数1:数据库路径(JDBC有格式要求，但是不同厂商在规定的格式中又有自身不同的地方)
            参数2:数据库的用户名
            参数3:数据库的密码
            返回值类型:Connection
            该方法返回的Connection实例就表示与数据库的一条连接。
            Connection是JDBC的一个核心接口，它用于表与数据库的一个连接，里面提供了创建执行对象，关闭连接等操作

            mysql的url固定格式
            jdbc:mysql://localhost:3306/birdbootdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true
                                        ^^^^^^^^^^
                                        数据库名称
                                        相当于USE birdbooddb
         */
        Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/birdbootdb?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true","root","root");
        System.out.println("与数据库建立连接!");

    }
}










