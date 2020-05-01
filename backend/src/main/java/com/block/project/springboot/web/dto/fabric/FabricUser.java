package com.block.project.springboot.web.dto.fabric;

import java.io.Serializable;

public class FabricUser implements Serializable
{
	private static final long serialVersionUID = 8077132186383604355L;

	private String userID;
	private String userPW;
	private String auth;
	private String refundAmount;
	private String refundDonations;
	private String organization;
	private String totalDonationAmount;
	private String organizedDonationProject;
	private String createdAt;

	public FabricUser(String userID, String userPW, String auth, String refundAmount, String refundDonations, String organization, String totalDonationAmount, String organizedDonationProject , String createdAt) {

		this.userID = userID;
		this.userPW = userPW;
		this.auth = auth;
		this.refundAmount = refundAmount;
		this.refundDonations = refundDonations;
		this.organization = organization;
		this.totalDonationAmount = totalDonationAmount;
		this.organizedDonationProject = organizedDonationProject;
		this.createdAt = createdAt;

	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserPW() {
		return userPW;
	}

	public void setUserPW(String userPW) {
		this.userPW = userPW;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundDonations() {
		return refundDonations;
	}

	public void setRefundDonations(String refundDonations) {
		this.refundDonations = refundDonations;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getTotalDonationAmount() {
		return totalDonationAmount;
	}

	public void setTotalDonationAmount(String totalDonationAmount) {
		this.totalDonationAmount = totalDonationAmount;
	}

	public String getOrganizedDonationProject() {
		return organizedDonationProject;
	}

	public void setOrganizedDonationProject(String organizedDonationProject) {
		this.organizedDonationProject = organizedDonationProject;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "FabricUser{" +
				"userID='" + userID + '\'' +
				", userPW='" + userPW + '\'' +
				", auth='" + auth + '\'' +
				", refundAmount='" + refundAmount + '\'' +
				", refundDonations='" + refundDonations + '\'' +
				", organization='" + organization + '\'' +
				", totalDonationAmount='" + totalDonationAmount + '\'' +
				", organizedDonationProject='" + organizedDonationProject + '\'' +
				", createdAt='" + createdAt + '\'' +
				'}';
	}
}
