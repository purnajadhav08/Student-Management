package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.example.demo.Databaseconfig.Db_Configuration;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import oracle.jdbc.OracleTypes;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class Courses_service {
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private Db_Configuration dbConfig;
    
    // procedure 4 to display prerequisites of requested course
    
    public void showPrerequisites(String deptCode, int courseNum) {
        try (Connection conn = dbConfig.dataSource().getConnection()) {
            // Call the stored procedure
            CallableStatement cs = conn.prepareCall("{call display_package.find_prerequisites(?, ?, ?)}");
            cs.setString(1, deptCode);
            cs.setInt(2, courseNum);
            cs.registerOutParameter(3, OracleTypes.CURSOR);
            cs.execute();

            // Retrieve the output from the stored procedure
            try (ResultSet rs = (ResultSet) cs.getObject(3)) {
                while (rs.next()) {
                    String prerequisite = rs.getString(1); // Assuming the first column is the prerequisite
                    System.out.println(prerequisite);
                }
            }

            // Close the statement
            cs.close();

        } catch (SQLException ex) {
            System.out.println("\n** SQLException caught **\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n** other Exception caught **\n");
        }
    }

    
    
  // procedure 2 to display tuples in courses table  

    public List<Map<String, Object>> showCourses() {
        List<Map<String, Object>> courses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            CallableStatement callableStatement = connection.prepareCall("{call display_package.show_courses(?)}");
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();
            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            while (resultSet.next()) {
                Map<String, Object> course = new HashMap<>();
                course.put("deptCode", resultSet.getString(1));
                course.put("courseNumber", resultSet.getInt(2));
                course.put("title", resultSet.getString(3));
                courses.add(course);
            }
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** Other Exception caught ***\n" + e.getMessage());
        }
        return courses;
    }

    
    // Add Courses
    
    public void addCourse(String deptCode, int courseNumber, String title) {
        try (Connection connection = dataSource.getConnection()) {
            try (CallableStatement callableStatement = connection.prepareCall("{call add_course(?, ?, ?)}")) {
                callableStatement.setString(1, deptCode);
                callableStatement.setInt(2, courseNumber);
                callableStatement.setString(3, title);
                callableStatement.executeUpdate();
                System.out.println("Course Added successfully.");
            }
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** Other Exception caught ***\n" + e.getMessage());
        }
    }
    
    
    //Delete Courses
    public void deleteCourse(String deptCode, int courseNumber) {
        try (Connection connection = dataSource.getConnection()) {
            // Call the stored procedure
            try (CallableStatement cs = connection.prepareCall("{call delete_course(?, ?)}")) {
                cs.setString(1, deptCode);
                cs.setInt(2, courseNumber);
                cs.executeUpdate();
                System.out.println("Course deleted successfully.");
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }


}
