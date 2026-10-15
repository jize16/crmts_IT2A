package com.scc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {
    public boolean registerUser(String full_name, String email, String password, String roles){

        String sql = "INSERT INTO users(full_name, email, password, roles, status) VALUES(?, ?, ?, ?, 'PENDING')";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, full_name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, roles.toUpperCase());
            pstmt.executeUpdate();
            System.out.println("-> Successful Registration, Waiting for Approval!");
            return true;

        }catch (SQLException e){
            System.err.println("Failed: "+ e.getMessage());
            return false;
        }

    }
}
