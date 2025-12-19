package com.voting.dao;

import com.voting.db.DBConnection;
import com.voting.model.Election;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ElectionDAO {

    public void addElection(Election e) throws SQLException {
        String sql = "INSERT INTO elections (year_of_election, name, description, election_date, " +
                "scheduled_start, scheduled_end, status, scope_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, e.getYearOfElection());
            ps.setString(2, e.getName());
            ps.setString(3, e.getDescription());
            ps.setDate(4, Date.valueOf(e.getElectionDate()));
            ps.setTimestamp(5, Timestamp.valueOf(e.getScheduledStart()));
            ps.setTimestamp(6, Timestamp.valueOf(e.getScheduledEnd()));
            ps.setString(7, e.getStatus());
            ps.setString(8, e.getScopeAddress());
            ps.executeUpdate();
        }
    }

    public void updateElectionStatus(int electionId, String status) throws SQLException {
        String sql = "UPDATE elections SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, electionId);
            ps.executeUpdate();
        }
    }

    public List<Election> getActiveElectionsForAddress(String address) throws SQLException {
        String sql = "SELECT * FROM elections WHERE status = 'RUNNING' AND scope_address = ?";
        List<Election> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, address);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<Election> getAllElections() throws SQLException {
        String sql = "SELECT * FROM elections";
        List<Election> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Election getElectionById(int id) throws SQLException {
        String sql = "SELECT * FROM elections WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    private Election mapRow(ResultSet rs) throws SQLException {
        Election e = new Election();
        e.setId(rs.getInt("id"));
        e.setYearOfElection(rs.getInt("year_of_election"));
        e.setName(rs.getString("name"));
        e.setDescription(rs.getString("description"));
        Date d = rs.getDate("election_date");
        if (d != null) {
            e.setElectionDate(d.toLocalDate());
        } else {
            e.setElectionDate(LocalDate.now());
        }
        Timestamp tsStart = rs.getTimestamp("scheduled_start");
        Timestamp tsEnd = rs.getTimestamp("scheduled_end");
        e.setScheduledStart(tsStart != null ? tsStart.toLocalDateTime() : LocalDateTime.now());
        e.setScheduledEnd(tsEnd != null ? tsEnd.toLocalDateTime() : LocalDateTime.now());
        e.setStatus(rs.getString("status"));
        e.setScopeAddress(rs.getString("scope_address"));
        return e;
    }
}
