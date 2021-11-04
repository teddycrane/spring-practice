package com.teddycrane.springpractice.models;

import java.util.Optional;
import java.util.UUID;

public class AddRacerRequest {
	private UUID raceId;
	private String firstName;
	private String lastName;

	public UUID getRaceId() {
		return raceId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
