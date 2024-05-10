package com.example.demo.services;

import com.example.demo.Databaseconfig.Db_Configuration;


import oracle.jdbc.OracleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class Logs_service {

    @Autowired
    private DataSource dataSource; // Autowire DataSource
    
    @Autowired
    private Db_Configuration dbConfig;

    


 
    // procedure 2 to display all the tuples in log table
    public List<Map<String, Object>> showLogs() {
        List<Map<String, Object>> logs = new ArrayList<>();

        try (Connection conn = dbConfig.dataSource().getConnection()) {
            CallableStatement storedProcedure = conn.prepareCall("{call display_package.show_logs(?)}");
            storedProcedure.registerOutParameter(1, OracleTypes.CURSOR);
            storedProcedure.execute();

            ResultSet resultSet = storedProcedure.getObject(1, ResultSet.class);

            while (resultSet.next()) {
                Map<String, Object> log = new HashMap<>();
                log.put("Log#", resultSet.getInt(1));
                log.put("User Name", resultSet.getString(2));
                log.put("Operation Time", resultSet.getDate(3));
                log.put("Table Name", resultSet.getString(4));
                log.put("Operation", resultSet.getString(5));
                log.put("Tuple Key Value", resultSet.getString(6));

                logs.add(log);
            }

            resultSet.close();
            storedProcedure.close();
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** Other Exception caught ***\n" + e.getMessage());
        }

        return logs;
    }


}
