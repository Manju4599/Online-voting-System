package com.voting.dao;

import com.voting.db.DBConnection;
import com.voting.model.Vote;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VoteDAO {

    public boolean hasUserVotedInElection(int userId, int electionId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM votes WHERE user_id = ? AND election_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void castVote(int userId, int electionId, int candidateId) throws SQLException {
        String sql = "INSERT INTO votes (user_id, election_id, candidate_id, vote_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, electionId);
            ps.setInt(3, candidateId);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        }
    }

    public List<Vote> getVotesByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM votes WHERE user_id = ?";
        List<Vote> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vote v = new Vote();
                    v.setId(rs.getInt("id"));
                    v.setUserId(rs.getInt("user_id"));
                    v.setElectionId(rs.getInt("election_id"));
                    v.setCandidateId(rs.getInt("candidate_id"));
                    Timestamp ts = rs.getTimestamp("vote_time");
                    v.setVoteTime(ts.toLocalDateTime());
                    list.add(v);
                }
            }
        }
        return list;
    }

    public void showElectionResults(int electionId) throws SQLException {
        String sql =
                "SELECT c.id, c.name, c.party, COUNT(v.id) AS vote_count " +
                "FROM candidates c LEFT JOIN votes v ON c.id = v.candidate_id " +
                "WHERE c.election_id = ? " +
                "GROUP BY c.id, c.name, c.party " +
                "ORDER BY vote_count DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, electionId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Results for election ID: " + electionId);
                while (rs.next()) {
                    int cid = rs.getInt("id");
                    String cname = rs.getString("name");
                    String party = rs.getString("party");
                    int count = rs.getInt("vote_count");
                    System.out.println("Candidate ID: " + cid + ", Name: " + cname +
                            ", Party: " + party + ", Votes: " + count);
                }
            }
        }
    }

    public void showUserVotingHistoryWithNames(int userId) throws SQLException {
        String sql =
                "SELECT v.vote_time, e.id AS election_id, e.name AS election_name, " +
                "c.id AS candidate_id, c.name AS candidate_name, c.party " +
                "FROM votes v " +
                "JOIN elections e ON v.election_id = e.id " +
                "JOIN candidates c ON v.candidate_id = c.id " +
                "WHERE v.user_id = ? " +
                "ORDER BY v.vote_time DESC";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    int electionId = rs.getInt("election_id");
                    String electionName = rs.getString("election_name");
                    int candidateId = rs.getInt("candidate_id");
                    String candidateName = rs.getString("candidate_name");
                    String party = rs.getString("party");
                    java.sql.Timestamp ts = rs.getTimestamp("vote_time");
                    System.out.println("Election: " + electionName + " (ID " + electionId + ")" +
                            ", Candidate: " + candidateName + " (ID " + candidateId + ", Party: " + party + ")" +
                            ", Time: " + ts.toLocalDateTime());
                }
                if (!any) {
                    System.out.println("No votes found.");
                }
            }
        }
    }
}
