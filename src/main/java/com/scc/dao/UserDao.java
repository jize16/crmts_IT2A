package com.scc.dao;

import com.scc.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    public UserDao() {

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            defaultAdminGenerator(conn);

        } catch (SQLException e) {
            System.err.println("Error seeding default admin: " + e.getMessage());
        }
    }

    public void defaultAdminGenerator(Connection conn) {
        String checkSql = "SELECT COUNT(*) FROM Users WHERE role = 'Administrator'";
        String insertSql = "INSERT INTO Users(full_name, email, password, role, status) "
                + "VALUES('admin', 'admin@seeker.com', 'admin123', 'Administrator', 'Active')";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate(insertSql);
                System.out.println(
                        "-> Default Administrator Created "
                                + "(Username: admin | Password: admin123)"
                );
            }

        } catch (SQLException e) {
            System.err.println("Error seeding default admin: " + e.getMessage());
        }
    }

    public boolean registerUser(String fullName, String email, String password, String role) {

        String sql = "INSERT INTO Users(full_name, email, password, role, status) VALUES(?, ?, ?, ?, 'PENDING')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);
            pstmt.executeUpdate();

            System.out.println("-- Successful Registration, Waiting for Approval");
            return true;

        } catch (SQLException e) {
            System.err.println("Failed: " + e.getMessage());
            return false;
        }
    }

    public User login(String full_name, String password) {

        String sql = "SELECT id, full_name, email, password, role, status "
                + "FROM Users WHERE full_name = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, full_name);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    if ("PENDING".equalsIgnoreCase(status)) {
                        System.out.println(" -- Account is pending, waiting for Approval");
                        return null;
                    }
                    return new User(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role"),
                            status);

                } else {
                    System.out.println(" -- INVALID username or password -- ");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return null;

    }

    public List<User> getAllUser() {
        List<User> usersList = new ArrayList<>();
        String sql = "SELECT id, full_name, email, password, role, status FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usersList.add(new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        }
        return usersList;

    }

        public List<User> getPendingUser(){
            List<User> pendingList = new ArrayList<>();
            String sql = "SELECT    * FROM users WHERE status = 'PENDING'";

            try(Connection conn = DatabaseConnection.getConnection()){
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while(rs.next()){
                    pendingList.add(new User(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("status")
                    ));
                }

            } catch (SQLException e) {
                System.err.println("Error fetching users: " + e.getMessage());
            }
            return pendingList;
            }

            public boolean approveUser(int userId){
                String sql = "UPDATE users SET status = 'APPROVED' WHERE id = ?";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, userId);
                    int rowsUpdated = pstmt.executeUpdate();

                    if(rowsUpdated > 0) {
                        System.out.print(" -> User id: "+userId+" approved successfully");
                        return true;
                    }else {
                        System.out.println(" -> ID not found.");
                    }

                } catch (Exception e){
                    System.err.println("Error approving users: "+e.getMessage());
                }
                return false;

            }


    }


