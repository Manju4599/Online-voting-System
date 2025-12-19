package com.voting.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Election {
    private int id;
    private int yearOfElection;
    private String name;
    private String description;
    private LocalDate electionDate;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private String status;
    private String scopeAddress;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getYearOfElection() {
        return yearOfElection;
    }
    public void setYearOfElection(int yearOfElection) {
        this.yearOfElection = yearOfElection;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getElectionDate() {
        return electionDate;
    }
    public void setElectionDate(LocalDate electionDate) {
        this.electionDate = electionDate;
    }

    public LocalDateTime getScheduledStart() {
        return scheduledStart;
    }
    public void setScheduledStart(LocalDateTime scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public LocalDateTime getScheduledEnd() {
        return scheduledEnd;
    }
    public void setScheduledEnd(LocalDateTime scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getScopeAddress() {
        return scopeAddress;
    }
    public void setScopeAddress(String scopeAddress) {
        this.scopeAddress = scopeAddress;
    }
}
