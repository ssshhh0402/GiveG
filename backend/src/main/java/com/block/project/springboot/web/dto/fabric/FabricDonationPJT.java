package com.block.project.springboot.web.dto.fabric;

public class FabricDonationPJT {

//    {"projectID":"qwerPJT","organizer":"qwer","createdAt":"2020-04-24 02:06:55",
//    "targetAmount":"120000","currentAmount":230400,"participants":["qwer2","qwer3"],
//    "projectGoalMap":"[[\"1\",{\"amount\":\"30\",\"compelete\":false,\"contents\":null}],[\"2\",{\"amount\":\"30\",\"compelete\":false,\"contents\":null}],[\"3\",{\"amount\":\"40\",\"compelete\":false,\"contents\":null}]]"
//    ,"fundraisingEndDate":"2020-05-20","state":"ProjectStarted"}

    private String projectID;
    private String organizer;
    private String createdAt;
    private String targetAmount;
    private String currentAmount;
    private String participants;
    private String projectGoalMap;
    private String fundraisingEndDate;
    private String state;

    public FabricDonationPJT(
             String projectID,
             String organizer,
             String createdAt,
             String targetAmount,
             String currentAmount,
             String participants,
             String projectGoalMap,
             String fundraisingEndDate,
             String state
    ){
        this.projectID = projectID;
        this.organizer = organizer;
        this.createdAt = createdAt;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.participants = participants;
        this.projectGoalMap = projectGoalMap;
        this.fundraisingEndDate = fundraisingEndDate;
        this.state = state;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(String targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(String currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getProjectGoalMap() {
        return projectGoalMap;
    }

    public void setProjectGoalMap(String projectGoalMap) {
        this.projectGoalMap = projectGoalMap;
    }

    public String getFundraisingEndDate() {
        return fundraisingEndDate;
    }

    public void setFundraisingEndDate(String fundraisingEndDate) {
        this.fundraisingEndDate = fundraisingEndDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "FabricDonationPJT{" +
                "projectID='" + projectID + '\'' +
                ", organizer='" + organizer + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", targetAmount='" + targetAmount + '\'' +
                ", currentAmount='" + currentAmount + '\'' +
                ", participants='" + participants + '\'' +
                ", projectGoalMap='" + projectGoalMap + '\'' +
                ", fundraisingEndDate='" + fundraisingEndDate + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
