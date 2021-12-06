package com.teddycrane.springpractice.models;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddRacerRequest
{

	@NotEmpty
	private List<UUID> racerIds;

	public AddRacerRequest()
	{
		racerIds = new ArrayList<>();
	}

	public AddRacerRequest(List<UUID> racerIds)
	{
		this.racerIds = new ArrayList<>(racerIds);
	}

	public List<UUID> getRacerIds()
	{
		return new ArrayList<>(this.racerIds);
	}
}
