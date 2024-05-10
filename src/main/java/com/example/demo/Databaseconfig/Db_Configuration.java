package com.example.demo.Databaseconfig;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Configuration
public class Db_Configuration {
	private Connection conn;
    private Statement stmt;
    private ResultSet rset;

	@Bean
	public DataSource dataSource() throws SQLException, IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        Properties prop = new Properties();
        prop.load(Db_Configuration.class.getClassLoader().getResourceAsStream("secret.properties"));

        String url = "jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:" + prop.getProperty("db_name");
        String username = prop.getProperty("db_user");
        String password = prop.getProperty("db_pass");

        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
	@Bean
    public Connection connection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
	}
	@PreDestroy
    public void cleanup() {
        try {
            if (rset != null) {
                rset.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught while closing resources ***\n");
            ex.printStackTrace();
        }
    }
}

