package com.example.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

@Service
public class ScoreGrade_service {

    @Autowired
    private DataSource dataSource;

  // procedure 2 to display all the tuples in scoregrades table
    public List<Map<String, Object>> showScoreGrades() {
        List<Map<String, Object>> scoreGrades = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            CallableStatement callableStatement = connection.prepareCall("{call display_package.show_score_grade(?)}");
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.execute();
            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            while (resultSet.next()) {
                Map<String, Object> scoreGrade = new HashMap<>();
                scoreGrade.put("score", resultSet.getDouble("score"));
                scoreGrade.put("lgrade", resultSet.getString("lgrade"));
                
                scoreGrades.add(scoreGrade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scoreGrades;
    }
}
