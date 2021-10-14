package com.teddycrane.springpractice.model;

public class Health {
	private String status;

	public Health(String status) {
		this.status = status;
	}

	public Health() {
		this("Healthy");
	}

	public String getStatus() {
		return this.status;
	}
}
