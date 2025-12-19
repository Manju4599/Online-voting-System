package com.voting.app;

import com.voting.dao.AdminDAO;
import com.voting.model.Admin;
import com.voting.model.User;
import com.voting.service.AdminService;
import com.voting.service.UserService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        AdminDAO adminDAO = new AdminDAO();
        AdminService adminService = new AdminService();

        while (true) {
            System.out.println("\n=== Online Voting System ===");
            System.out.println("1. User login");
            System.out.println("2. User sign-up");
            System.out.println("3. Admin login");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    User user = userService.login(sc);
                    if (user != null) {
                        userService.userMenu(sc, user);
                    }
                    break;
                case "2":
                    userService.signUp(sc);
                    break;
                case "3":
                    Admin admin = handleAdminLogin(sc, adminDAO);
                    if (admin != null) {
                        adminService.adminMenu(sc);
                    }
                    break;
                case "4":
                    System.out.println("Exiting application.");
                    sc.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static Admin handleAdminLogin(Scanner sc, AdminDAO adminDAO) {
        try {
            System.out.print("Enter admin email: ");
            String email = sc.nextLine();
            System.out.print("Enter admin password: ");
            String password = sc.nextLine();
            Admin admin = adminDAO.login(email, password);
            if (admin == null) {
                System.out.println("Invalid admin credentials.");
            } else {
                System.out.println("Welcome admin, " + admin.getName());
            }
            return admin;
        } catch (Exception e) {
            System.out.println("Error during admin login: " + e.getMessage());
            return null;
        }
    }
}
