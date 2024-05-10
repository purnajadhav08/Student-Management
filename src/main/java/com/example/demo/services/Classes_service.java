package com.example.demo.services;
import com.example.demo.Databaseconfig.Db_Configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Classes_service {
	@Autowired
    private DataSource dataSource;
	
	 @Autowired
	    private Db_Configuration dbConfig;
	// procedure 2 to display tuples in classes table
	 
	 public List<Map<String, Object>> showClasses() {
		    List<Map<String, Object>> classes = new ArrayList<>();

		    try (Connection connection = dataSource.getConnection()) {
		        CallableStatement callableStatement = connection.prepareCall("{call display_package.show_classes(?)}");
		        callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
		        callableStatement.execute();
		        ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

		        while (resultSet.next()) {
		            Map<String, Object> classInfo = new HashMap<>();
		            classInfo.put("ClassId", resultSet.getString(1));
		            classInfo.put("DeptCode", resultSet.getString(2));
		            classInfo.put("CourseNumber", resultSet.getInt(3));
		            classInfo.put("SectionNumber", resultSet.getInt(4));
		            classInfo.put("Year", resultSet.getInt(5));
		            classInfo.put("Semester", resultSet.getString(6));
		            classInfo.put("Limit", resultSet.getInt(7));
		            classInfo.put("ClassSize", resultSet.getInt(8));
		            classInfo.put("Room", resultSet.getString(9));

		            classes.add(classInfo);
		        }
		    } catch (SQLException ex) {
		        System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
		    } catch (Exception e) {
		        System.out.println("\n*** Other Exception caught ***\n" + e.getMessage());
		    }

		    return classes;
		}

	
	//Add classes
	 public void addClass(String classId, String deptCode, int courseNumber, int sectionNumber, int year, String semester, int limit, int classSize, String room) {
		    try (Connection connection = dataSource.getConnection()) {
		        try (CallableStatement cs = connection.prepareCall("{call display_package.add_class(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
		            cs.setString(1, classId);
		            cs.setString(2, deptCode);
		            cs.setInt(3, courseNumber);
		            cs.setInt(4, sectionNumber);
		            cs.setInt(5, year);
		            cs.setString(6, semester);
		            cs.setInt(7, limit);
		            cs.setInt(8, classSize);
		            cs.setString(9, room);
		            cs.executeUpdate();
		            System.out.println("Class added successfully.");
		        }
		    } catch (SQLException ex) {
		        System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
		    } catch (Exception e) {
		        System.out.println("\n*** Other Exception caught ***\n" + e.getMessage());
		    }
		}

		public void deleteClass(String classId) {
		    try (Connection connection = dataSource.getConnection()) {
		        try (CallableStatement cs = connection.prepareCall("{call display_package.delete_class(?)}")) {
		            cs.setString(1, classId);
		            cs.executeUpdate();
		            System.out.println("Class deleted successfully.");
		        }
		    } catch (SQLException ex) {
		        System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
		    } catch (Exception e) {
		        System.out.println("\n*** Other Exception caught ***\n" + e.getMessage());
		    }
		}

}
