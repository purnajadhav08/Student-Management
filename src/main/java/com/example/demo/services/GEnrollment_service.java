package com.example.demo.services;

import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Databaseconfig.Db_Configuration;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

@Service
public class GEnrollment_service {

    @Autowired
    private Db_Configuration dbConfig;
    
    @Autowired
    private DataSource dataSource;
    
    // procedure 5 to enroll a student in class 
    
    public String enrollStudentIntoClass(String BNumber, String classId) {
    	try (Connection conn = dataSource.getConnection()) {
            // Call the stored procedure
            CallableStatement cs = conn.prepareCall("{call display_package.enroll_student_into_class(?, ?)}");
            cs.setString(1, BNumber);
            cs.setString(2, classId);
            cs.execute();

            // Close the statement
            cs.close();

            return "Enrollment successful.";
        } catch (SQLException ex) {
            return "SQL Exception caught: " + ex.getMessage();
        } catch (Exception e) {
            return "Other Exception caught";
        }
    }
    
    // procedure 6 to drop a student from class
    
    public String dropStudentFromClass(String Bnum, String Classid) {
        try (Connection conn = dataSource.getConnection()) {
            // Call the stored procedure
            CallableStatement cs = conn.prepareCall("{call display_package.drop_student(?, ?)}");
            cs.setString(1, Bnum);
            cs.setString(2, Classid);
            cs.execute();
            return "Student dropped from class successfully.";
        } catch (SQLException ex) {
            return "SQL Exception caught: " + ex.getMessage();
        } catch (Exception e) {
            return "Other Exception caught";
        }
    }

   
// procedure 2 to display all tuples in g_enrollments table

    public List<Map<String, Object>> showEnrollments() {
        List<Map<String, Object>> enrollments = new ArrayList<>();

        try (Connection conn = dbConfig.dataSource().getConnection()) {
            // Call the stored procedure
            CallableStatement stdinfo = conn.prepareCall("{call display_package.show_g_enrollments(?)}");
            stdinfo.registerOutParameter(1, OracleTypes.CURSOR);

            // Execute and retrieve the result set
            stdinfo.execute();
            ResultSet resultSet = stdinfo.getObject(1, ResultSet.class);

            // Process the result set
            while (resultSet.next()) {
                Map<String, Object> enrollment = new HashMap<>();
                enrollment.put("BNumber", resultSet.getString(1));
                enrollment.put("ClassId", resultSet.getString(2));
                enrollment.put("Score", resultSet.getDouble(3));
                enrollments.add(enrollment);
            }

            // Close the result set and statement
            resultSet.close();
            stdinfo.close();
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** Other Exception caught ***\n" + e.getMessage());
        }

        return enrollments;
    }
}
