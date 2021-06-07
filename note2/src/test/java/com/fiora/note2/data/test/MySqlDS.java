package com.fiora.note2.data.test;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;


public class MySqlDS {
    // thread safe
    private static DataSource ds;

    static {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(3306);
        dataSource.setDatabaseName("test");
        dataSource.setUser("root");
        dataSource.setPassword("Sunshine8023");
        try {
            dataSource.setAllowMultiQueries(true);
            dataSource.setServerTimezone("");
            dataSource.setRewriteBatchedStatements(true); // To get the actual benefits of Batch Processing in MySQL
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ds = dataSource;
    }

    private MySqlDS() {
    }

    public static DataSource getDs() {
        return ds;
    }
}
