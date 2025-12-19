package com.voting.model;

import java.time.LocalDateTime;

public class Vote {
    private int id;
    private int userId;
    private int electionId;
    private int candidateId;
    private LocalDateTime voteTime;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getElectionId() {
        return electionId;
    }
    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }

    public int getCandidateId() {
        return candidateId;
    }
    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public LocalDateTime getVoteTime() {
        return voteTime;
    }
    public void setVoteTime(LocalDateTime voteTime) {
        this.voteTime = voteTime;
    }
}
