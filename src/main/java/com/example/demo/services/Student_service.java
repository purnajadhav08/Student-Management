package com.example.demo.services;


import com.example.demo.Databaseconfig.Db_Configuration;

import com.example.demo.services.Logs_service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Date;
import java.util.List;
import java.util.Map;
import java.sql.Date;

import oracle.jdbc.OracleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.Databaseconfig.Db_Configuration;

@Service
public class Student_service {
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private Logs_service logsService;
    
    @Autowired
    private Db_Configuration dbConfig;
    
    // Procedure to add a student in table
    
    public void add_students(String BNumber, String firstname, String lastname, String status, Double gpa, String email, java.util.Date bdate) {
        try (Connection conn = dbConfig.dataSource().getConnection()) {

            // call to stored procedure
            CallableStatement cs = conn.prepareCall("begin display_package.add_students(?, ?, ?, ?, ?, ?, ?, ?); end;");

            // set parameters
            cs.setString(1, BNumber);
            cs.setString(2, firstname);
            cs.setString(3, lastname);
            cs.setString(4, status);
            cs.setDouble(5, gpa);
            cs.setString(6, email);
            cs.setDate(7, new java.sql.Date(bdate.getTime()));
            cs.registerOutParameter(8, Types.VARCHAR);

            // execute and retrieve the result set
            cs.execute();

            // get the out parameter result.
            String message = cs.getString(8);
            //System.out.println(message);
            System.out.println("Student Added successfully.");

            // close the statement
            cs.close();

        } catch (SQLException ex) {
            System.out.println("\n** SQLException caught **\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n** other Exception caught **\n");
        }
    }

   // procedure to get all students in class by userinput=classid
    
    public void listStudents(String classId) {
        try (Connection conn = dbConfig.dataSource().getConnection()) {
            // Call the stored procedure
            CallableStatement cs = conn.prepareCall("{call display_package.list_students_in_class(?, ?)}");
            cs.setString(1, classId);
            cs.registerOutParameter(2, OracleTypes.CURSOR);
            cs.execute();

            // Retrieve the result set
            ResultSet rs = (ResultSet) cs.getObject(2);

            // Print the results
            while (rs.next()) {
                String bNumber = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                System.out.println(bNumber + ", " + firstName + " " + lastName);
            }

            // Close the result set and statement
            rs.close();
            cs.close();

        } catch (SQLException ex) {
            System.out.println("\n** SQLException caught **\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n** other Exception caught **\n");
        }
    }
    
    // procedure to delete a student from table and using trigger to show
    // the delete activity in logs
    
    public void deleteStudent(String bnum) {
    	try (Connection conn = dbConfig.dataSource().getConnection()){
    		CallableStatement cs = conn.prepareCall("{call delete_student(?)}");
            cs.setString(1, bnum);
            cs.execute();
            System.out.println("Student with B# " + bnum + " deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }


    // procedure 2 to show all tuples in students table
    public List<Map<String, Object>> showStudents() {
        List<Map<String, Object>> students = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall("{call display_package.show_students(?)}")) {
             
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();
            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            while (resultSet.next()) {
                Map<String, Object> student = new HashMap<>();
                student.put("BNumber", resultSet.getString(1));
                student.put("FirstName", resultSet.getString(2));
                student.put("LastName", resultSet.getString(3));
                student.put("Level", resultSet.getString(4));
                student.put("Gpa", resultSet.getDouble(5));
                student.put("Email", resultSet.getString(6));
                student.put("BirthDate", resultSet.getDate(7));

                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving students: " + e.getMessage());
        }

        return students;
    }
    
}
