package com.voting.dao;

import com.voting.db.DBConnection;
import com.voting.model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateDAO {

    public void addCandidate(Candidate c) throws SQLException {
        String sql = "INSERT INTO candidates (name, party, election_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getParty());
            ps.setInt(3, c.getElectionId());
            ps.executeUpdate();
        }
    }

    public void updateCandidate(Candidate c) throws SQLException {
        String sql = "UPDATE candidates SET name = ?, party = ?, election_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getParty());
            ps.setInt(3, c.getElectionId());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        }
    }

    public void deleteCandidate(int id) throws SQLException {
        String sql = "DELETE FROM candidates WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Candidate> getCandidatesByElection(int electionId) throws SQLException {
        String sql = "SELECT * FROM candidates WHERE election_id = ?";
        List<Candidate> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Candidate c = new Candidate();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    c.setParty(rs.getString("party"));
                    c.setElectionId(rs.getInt("election_id"));
                    list.add(c);
                }
            }
        }
        return list;
    }

    public List<Candidate> getAllCandidates() throws SQLException {
        String sql = "SELECT * FROM candidates";
        List<Candidate> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Candidate c = new Candidate();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setParty(rs.getString("party"));
                c.setElectionId(rs.getInt("election_id"));
                list.add(c);
            }
        }
        return list;
    }
}
