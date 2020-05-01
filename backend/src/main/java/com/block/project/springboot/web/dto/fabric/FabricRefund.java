package com.block.project.springboot.web.dto.fabric;

public class FabricRefund {
//    {"amount":0,"state":"Required","createdAt":"2020-04-25 06:04:26"}

    private String userID;
    private String projectID;
    private String state;
    private String createdAt;
    private String amount;

    public FabricRefund(
            String userID,
            String projectID,
            String state,
            String createdAt,
            String amount
    ){
        this.userID = userID;
        this.projectID = projectID;
        this.state = state;
        this.createdAt = createdAt;
        this.amount = amount;
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

    public void setProjectID(String pojectID) {
        this.projectID = pojectID;
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

    public void setCreatedAt(String createAt) {
        this.createdAt = createAt;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "FabricRefund{" +
                "userID='" + userID + '\'' +
                ", projectID='" + projectID + '\'' +
                ", state='" + state + '\'' +
                ", createAt='" + createdAt + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
