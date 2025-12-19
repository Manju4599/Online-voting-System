package com.voting.service;

import com.voting.dao.CandidateDAO;
import com.voting.dao.ElectionDAO;
import com.voting.dao.UserDAO;
import com.voting.dao.VoteDAO;
import com.voting.model.Candidate;
import com.voting.model.Election;
import com.voting.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class AdminService {

    private final ElectionDAO electionDAO = new ElectionDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final UserDAO userDAO = new UserDAO();
    private final VoteDAO voteDAO = new VoteDAO();

    public void adminMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Manage elections");
            System.out.println("2. Manage candidates");
            System.out.println("3. Manage voters");
            System.out.println("4. Manage results");
            System.out.println("5. Logout");
            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    manageElections(sc);
                    break;
                case "2":
                    manageCandidates(sc);
                    break;
                case "3":
                    manageVoters(sc);
                    break;
                case "4":
                    manageResults(sc);
                    break;
                case "5":
                    System.out.println("Admin logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void manageElections(Scanner sc) {
        while (true) {
            System.out.println("\n--- Manage Elections ---");
            System.out.println("1. Add election");
            System.out.println("2. Start election");
            System.out.println("3. Terminate election");
            System.out.println("4. List elections");
            System.out.println("5. Back");
            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addElection(sc);
                        break;
                    case "2":
                        changeElectionStatus(sc, "RUNNING");
                        break;
                    case "3":
                        changeElectionStatus(sc, "ENDED");
                        break;
                    case "4":
                        listElections();
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error managing elections: " + e.getMessage());
            }
        }
    }

    private void addElection(Scanner sc) throws SQLException {
        Election e = new Election();
        System.out.print("Enter year of election: ");
        e.setYearOfElection(Integer.parseInt(sc.nextLine()));
        System.out.print("Enter election name: ");
        e.setName(sc.nextLine());
        System.out.print("Enter description: ");
        e.setDescription(sc.nextLine());
        System.out.print("Enter election date (YYYY-MM-DD): ");
        e.setElectionDate(LocalDate.parse(sc.nextLine()));
        System.out.print("Enter scheduled start (YYYY-MM-DDTHH:MM): ");
        e.setScheduledStart(LocalDateTime.parse(sc.nextLine()));
        System.out.print("Enter scheduled end (YYYY-MM-DDTHH:MM): ");
        e.setScheduledEnd(LocalDateTime.parse(sc.nextLine()));
        System.out.print("Enter scope address (state/district/city/area or * for all): ");
        e.setScopeAddress(sc.nextLine());
        e.setStatus("SCHEDULED");

        electionDAO.addElection(e);
        System.out.println("Election added.");
    }

    private void changeElectionStatus(Scanner sc, String status) throws SQLException {
        listElections();
        System.out.print("Enter election ID: ");
        int id = Integer.parseInt(sc.nextLine());
        electionDAO.updateElectionStatus(id, status);
        System.out.println("Election status updated to " + status + ".");
    }

    private void listElections() throws SQLException {
        List<Election> list = electionDAO.getAllElections();
        System.out.println("--- Elections ---");
        for (Election e : list) {
            System.out.println(e.getId() + ": " + e.getName() + " (" + e.getYearOfElection() +
                    "), Status: " + e.getStatus() + ", Scope: " + e.getScopeAddress());
        }
    }

    private void manageCandidates(Scanner sc) {
        while (true) {
            System.out.println("\n--- Manage Candidates ---");
            System.out.println("1. Add candidate");
            System.out.println("2. Update candidate");
            System.out.println("3. Delete candidate");
            System.out.println("4. List candidates");
            System.out.println("5. Back");
            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addCandidate(sc);
                        break;
                    case "2":
                        updateCandidate(sc);
                        break;
                    case "3":
                        deleteCandidate(sc);
                        break;
                    case "4":
                        listCandidates();
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error managing candidates: " + e.getMessage());
            }
        }
    }

    private void addCandidate(Scanner sc) throws SQLException {
        Candidate c = new Candidate();
        System.out.print("Enter candidate name: ");
        c.setName(sc.nextLine());
        System.out.print("Enter party: ");
        c.setParty(sc.nextLine());
        listElections();
        System.out.print("Enter election ID for this candidate: ");
        c.setElectionId(Integer.parseInt(sc.nextLine()));
        candidateDAO.addCandidate(c);
        System.out.println("Candidate added.");
    }

    private void updateCandidate(Scanner sc) throws SQLException {
        listCandidates();
        System.out.print("Enter candidate ID to update: ");
        int id = Integer.parseInt(sc.nextLine());
        Candidate c = new Candidate();
        c.setId(id);
        System.out.print("Enter new name: ");
        c.setName(sc.nextLine());
        System.out.print("Enter new party: ");
        c.setParty(sc.nextLine());
        listElections();
        System.out.print("Enter new election ID: ");
        c.setElectionId(Integer.parseInt(sc.nextLine()));
        candidateDAO.updateCandidate(c);
        System.out.println("Candidate updated.");
    }

    private void deleteCandidate(Scanner sc) throws SQLException {
        listCandidates();
        System.out.print("Enter candidate ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());
        candidateDAO.deleteCandidate(id);
        System.out.println("Candidate deleted.");
    }

    private void listCandidates() throws SQLException {
        List<Candidate> list = candidateDAO.getAllCandidates();
        System.out.println("--- Candidates ---");
        for (Candidate c : list) {
            System.out.println(c.getId() + ": " + c.getName() + " (" + c.getParty() +
                    "), Election ID: " + c.getElectionId());
        }
    }

    private void manageVoters(Scanner sc) {
        while (true) {
            System.out.println("\n--- Manage Voters ---");
            System.out.println("1. List voters");
            System.out.println("2. Update voter age/phone");
            System.out.println("3. Delete voter");
            System.out.println("4. Back");
            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1":
                        listVoters();
                        break;
                    case "2":
                        updateVoter(sc);
                        break;
                    case "3":
                        deleteVoter(sc);
                        break;
                    case "4":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error managing voters: " + e.getMessage());
            }
        }
    }

    private void listVoters() throws SQLException {
        List<User> list = userDAO.getAllUsers();
        System.out.println("--- Voters ---");
        for (User u : list) {
            System.out.println(u.getId() + ": " + u.getName() + ", Email: " + u.getEmail() +
                    ", Phone: " + u.getPhone() + ", Age: " + u.getAge() +
                    ", Address: " + u.getAddress());
        }
    }

    private void updateVoter(Scanner sc) throws SQLException {
        listVoters();
        System.out.print("Enter voter ID to update: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Enter new age: ");
        int age = Integer.parseInt(sc.nextLine());
        System.out.print("Enter new phone: ");
        String phone = sc.nextLine();
        userDAO.updateContactAndAge(id, age, phone);
        System.out.println("Voter updated.");
    }

    private void deleteVoter(Scanner sc) throws SQLException {
        listVoters();
        System.out.print("Enter voter ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());
        userDAO.deleteUser(id);
        System.out.println("Voter deleted.");
    }

    private void manageResults(Scanner sc) {
        while (true) {
            System.out.println("\n--- Manage Results ---");
            System.out.println("1. Announce result (mark RESULT_DECLARED)");
            System.out.println("2. Show previous election results");
            System.out.println("3. Back");
            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1":
                        announceResult(sc);
                        break;
                    case "2":
                        showAllResults();
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error managing results: " + e.getMessage());
            }
        }
    }

    private void announceResult(Scanner sc) throws SQLException {
        listElections();
        System.out.print("Enter election ID to declare result: ");
        int id = Integer.parseInt(sc.nextLine());
        electionDAO.updateElectionStatus(id, "RESULT_DECLARED");
        System.out.println("Status set to RESULT_DECLARED. Current tallies:");
        voteDAO.showElectionResults(id);
    }

    private void showAllResults() throws SQLException {
        List<Election> list = electionDAO.getAllElections();
        System.out.println("--- Previous election results ---");
        for (Election e : list) {
            if (!"SCHEDULED".equalsIgnoreCase(e.getStatus())) {
                System.out.println("\nElection ID: " + e.getId() + ", Name: " + e.getName() +
                        ", Status: " + e.getStatus());
                voteDAO.showElectionResults(e.getId());
            }
        }
    }
}
