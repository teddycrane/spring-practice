package com.teddycrane.springpractice.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddRacerRequest {
	private UUID raceId;
	private List<UUID> racerIds;

	public UUID getRaceId() {
		return raceId;
	}

	public List<UUID> getRacerIds() {
		return new ArrayList<>(this.racerIds);
	}
}
