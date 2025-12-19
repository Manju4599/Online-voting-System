package com.voting.model;

public class Candidate {
    private int id;
    private String name;
    private String party;
    private int electionId;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }
    public void setParty(String party) {
        this.party = party;
    }

    public int getElectionId() {
        return electionId;
    }
    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }
}
