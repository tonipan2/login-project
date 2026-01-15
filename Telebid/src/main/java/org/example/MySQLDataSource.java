package org.example;
import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;

public class MySQLDataSource {
    private static final MysqlDataSource ds = new MysqlDataSource();

    static {
        ds.setURL("jdbc:mysql://localhost:3306/telebid");
        ds.setUser("root");
        ds.setPassword("rycbar123");
    }

    public static DataSource getDataSource() {
        return ds;
    }
}

