package com.block.project.springboot.web.dto.fabric;

public class FabricDonate {

    private String userID;
    private String projectID;
    private String amount;
    private String state;
    private String createdAt;

    public FabricDonate(
            String userID,
            String projectID,
            String amount,
            String state,
            String createdAt
    ) {
        this.userID = userID;
        this.projectID = projectID;
        this.amount = amount;
        this.state = state;
        this.createdAt = createdAt;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projecID) {
        this.projectID = projecID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "FabricDonate{" +
                "userID='" + userID + '\'' +
                ", projecID='" + projectID + '\'' +
                ", amount='" + amount + '\'' +
                ", state='" + state + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
