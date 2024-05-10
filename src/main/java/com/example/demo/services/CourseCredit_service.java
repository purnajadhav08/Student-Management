package com.example.demo.services;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Databaseconfig.Db_Configuration;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class CourseCredit_service {

    @Autowired
    private Db_Configuration dbConfig;

 // procedure 2 to display tuples in coursecredits table
    
    public void showCourseCredits() {
        try (Connection conn = dbConfig.dataSource().getConnection()) {
            CallableStatement storedProcedure = conn.prepareCall("{call display_package.show_course_credit(?)}");
            storedProcedure.registerOutParameter(1, OracleTypes.CURSOR);
            storedProcedure.execute();

            ResultSet resultSet = storedProcedure.getObject(1, ResultSet.class);

            while (resultSet.next()) {
                // Assuming the result set has three columns
                System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2));
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
