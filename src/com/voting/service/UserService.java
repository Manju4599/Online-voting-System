package com.voting.service;

import com.voting.dao.CandidateDAO;
import com.voting.dao.ElectionDAO;
import com.voting.dao.UserDAO;
import com.voting.dao.VoteDAO;
import com.voting.model.Candidate;
import com.voting.model.Election;
import com.voting.model.User;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserService {

    private final UserDAO userDAO = new UserDAO();
    private final ElectionDAO electionDAO = new ElectionDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final VoteDAO voteDAO = new VoteDAO();


    public void signUp(Scanner sc) {
    try {
        User u = new User();
        System.out.print("Enter name: ");
        u.setName(sc.nextLine());

        int age;
        while (true) {
            System.out.print("Enter age (must be >= 18): ");
            String ageStr = sc.nextLine();
            try {
                age = Integer.parseInt(ageStr);
                if (age < 18) {
                    System.out.println("You must be at least 18 years old to register.");
                } else {
                    break;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number for age.");
            }
        }
        u.setAge(age);

        System.out.print("Enter email: ");
        u.setEmail(sc.nextLine());
        System.out.print("Enter phone: ");
        u.setPhone(sc.nextLine());
        System.out.print("Enter password: ");
        u.setPassword(sc.nextLine());
        System.out.print("Enter address (state/district/city/area): ");
        u.setAddress(sc.nextLine());

        userDAO.registerUser(u);
        System.out.println("User registered successfully.");
    } catch (Exception e) {
        System.out.println("Error during sign-up: " + e.getMessage());
    }
}


    public User login(Scanner sc) {
        try {
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            User u = userDAO.login(email, password);
            if (u == null) {
                System.out.println("Invalid credentials.");
            } else {
                System.out.println("Welcome, " + u.getName());
            }
            return u;
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            return null;
        }
    }

    public void userMenu(Scanner sc, User user) {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Vote");
            System.out.println("2. Update age/phone");
            System.out.println("3. Manage address");
            System.out.println("4. Voting history and results");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    handleVoting(sc, user);
                    break;
                case "2":
                    updateContact(sc, user);
                    break;
                case "3":
                    updateAddress(sc, user);
                    break;
                case "4":
                    showHistoryAndResults(user);
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void handleVoting(Scanner sc, User user) {
        try {
            List<Election> elections = electionDAO.getActiveElectionsForAddress(user.getAddress());
            if (elections.isEmpty()) {
                System.out.println("No active elections for your address.");
                return;
            }
            System.out.println("Active elections for your address:");
            for (Election e : elections) {
                System.out.println(e.getId() + " - " + e.getName() + " (" + e.getYearOfElection() + ")");
            }
            System.out.print("Enter election ID to vote (or 0 to back): ");
            int eid = Integer.parseInt(sc.nextLine());
            if (eid == 0) return;

            if (voteDAO.hasUserVotedInElection(user.getId(), eid)) {
                System.out.println("You have already voted in this election.");
                return;
            }

            List<Candidate> candidates = candidateDAO.getCandidatesByElection(eid);
            if (candidates.isEmpty()) {
                System.out.println("No candidates for this election.");
                return;
            }
            System.out.println("Candidates:");
            for (Candidate c : candidates) {
                System.out.println(c.getId() + " - " + c.getName() + " (" + c.getParty() + ")");
            }
            System.out.print("Enter candidate ID to vote (or 0 to back): ");
            int cid = Integer.parseInt(sc.nextLine());
            if (cid == 0) return;

            voteDAO.castVote(user.getId(), eid, cid);
            System.out.println("Vote cast successfully.");
        } catch (Exception e) {
            System.out.println("Error while voting: " + e.getMessage());
        }
    }

    private void updateContact(Scanner sc, User user) {
        try {
            System.out.print("Enter new age: ");
            int age = Integer.parseInt(sc.nextLine());
            System.out.print("Enter new phone: ");
            String phone = sc.nextLine();
            userDAO.updateContactAndAge(user.getId(), age, phone);
            user.setAge(age);
            user.setPhone(phone);
            System.out.println("Updated age and phone.");
        } catch (Exception e) {
            System.out.println("Error updating contact: " + e.getMessage());
        }
    }

    private void updateAddress(Scanner sc, User user) {
        try {
            System.out.print("Enter new address (state/district/city/area): ");
            String addr = sc.nextLine();
            userDAO.updateAddress(user.getId(), addr);
            user.setAddress(addr);
            System.out.println("Address updated.");
        } catch (Exception e) {
            System.out.println("Error updating address: " + e.getMessage());
        }
    }

    private void showHistoryAndResults(User user) {
        try {
            System.out.println("--- Your voting history ---");
            voteDAO.showUserVotingHistoryWithNames(user.getId());

            System.out.println("\n--- All election results ---");
            List<Election> elections = electionDAO.getAllElections();
            for (Election e : elections) {
                System.out.println("\nElection ID: " + e.getId() + ", Name: " + e.getName() +
                        " (" + e.getYearOfElection() + "), Status: " + e.getStatus());
                voteDAO.showElectionResults(e.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error fetching history/results: " + e.getMessage());
        }
    }

}
