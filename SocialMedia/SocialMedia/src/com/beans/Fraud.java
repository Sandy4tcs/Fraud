package com.beans;

public class Fraud {

	private String fraudCategory;
	private Double fraudScore;
	private String Score;
	private String fraudLink;
	private String post;
	private String message;
	private String m_time;
	public String getM_time() {
		return m_time;
	}
	public void setM_time(String m_time) {
		this.m_time = m_time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getScore() {
		return Score;
	}
	public void setScore(String score) {
		Score = score;
	}
	public String getFraudCategory() {
		return fraudCategory;
	}
	public void setFraudCategory(String fraudCategory) {
		this.fraudCategory = fraudCategory;
	}
	public Double getFraudScore() {
		return fraudScore;
	}
	public void setFraudScore(double score2) {
		this.fraudScore = score2;
	}
	public String getFraudLink() {
		return fraudLink;
	}
	public void setFraudLink(String fraudLink) {
		this.fraudLink = fraudLink;
	}
	
}
