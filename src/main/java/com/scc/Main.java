package com.scc;

import com.scc.dao.UserDao;
import com.scc.model.User;
import com.scc.ui.AdminMenu;

import java.util.Scanner;

public class Main {

        private static final Scanner sc = new Scanner(System.in);
        private static final UserDao userDao = new UserDao();

        public static void main(String[] args){

            System.out.println("\n================================================");
            System.out.println("\n COMPUTER REPAIR & MAINTENANCE TRACKING SYSTEM");
            System.out.println("\n================================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice){
                case "1":
                    handleLogin();
                    break;
                case "2":
                    System.out.print("Enter Full Name: ");
                    String full_name = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    System.out.print("\nSelect Role "+
                            "1] Service Staff " +
                            "[2] Technician [" +
                            "3] Customer/Client: ");

                    int roleChoice = sc.nextInt();
                        String role;
                        switch (roleChoice){
                            case 1:
                                role = "SERVICE STAFF";
                            break;
                            case 2:
                                role = "TECHNICIAN";
                            break;
                            case 3:
                                role = "CUSTOMER/CLIENT";
                            break;
                            default:
                                role = null;
                        }
                        if (role != null){
                            userDao.registerUser( full_name, email, password, role);


                        }else{

                            System.out.println("Invalid roles Selection!");
                        }
                        break;

            }

        }

        private static void handleLogin(){
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            User user = userDao.login(username, password);

            if(user != null){
                System.out.println("\n -> WELCOME "+user.full_name()+" | ROLE: " +user.role()+"");
                switch (user.role().toUpperCase()) {
                    case "ADMINISTRATOR":
                    case "ADMIN":
                        new AdminMenu(userDao, sc).display(user);
                        break;

                    default:
                        System.out.println("Account role not found!");
                }
            }

        }
}


