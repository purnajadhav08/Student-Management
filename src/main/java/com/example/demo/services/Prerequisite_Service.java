package com.example.demo.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.Databaseconfig.Db_Configuration;
import org.springframework.stereotype.Service;

import oracle.jdbc.OracleTypes;

@Service
public class Prerequisite_Service {
	
	@Autowired
    private Db_Configuration dbConfig;
	
	
	
	
	
// Procedure 2 to display all the tuples in prerequisites table
    public void showPrerequisite() {
        try (Connection conn = dbConfig.dataSource().getConnection()) {
            CallableStatement storedProcedure = conn.prepareCall("{call display_package.show_prerequisites(?)}");
            storedProcedure.registerOutParameter(1, OracleTypes.CURSOR);
            storedProcedure.execute();

            ResultSet resultSet = storedProcedure.getObject(1, ResultSet.class);

            while (resultSet.next()) {
                // Assuming the result set has three columns
                System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getString(3)+ "\t" + resultSet.getString(4));
            }

            resultSet.close();
            storedProcedure.close();
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** Other Exception caught ***\n" + e.getMessage());
        }
    }

}
